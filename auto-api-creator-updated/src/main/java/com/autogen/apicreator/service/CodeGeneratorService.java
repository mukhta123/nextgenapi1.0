package com.autogen.apicreator.service;

import com.autogen.apicreator.model.ModelDefinition;
import com.autogen.apicreator.util.FileWriterUtil;
import com.autogen.apicreator.util.DynamicEndpointRegistrar;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CodeGeneratorService {

    private final AIAgentService aiAgentService;
    private final DynamicEndpointRegistrar endpointRegistrar;

    // store generated service code in-memory for dynamic handlers to use
    private final Map<String, String> generatedServiceMap = new ConcurrentHashMap<>();

    public CodeGeneratorService(AIAgentService aiAgentService, DynamicEndpointRegistrar endpointRegistrar) {
        this.aiAgentService = aiAgentService;
        this.endpointRegistrar = endpointRegistrar;
    }

    public String generateApi(ModelDefinition modelDef) {
        String modelCode = generateModel(modelDef);
        String serviceCode = aiAgentService.generateBusinessLogic(modelDef);
        String controllerCode = generateController(modelDef);

        // write files
        FileWriterUtil.writeToFile("generated/models/" + modelDef.getModelName() + ".java", modelCode);
        FileWriterUtil.writeToFile("generated/services/" + modelDef.getModelName() + "Service.java", serviceCode);
        FileWriterUtil.writeToFile("generated/controllers/" + modelDef.getModelName() + "Controller.java", controllerCode);

        // keep service code in memory
        generatedServiceMap.put(modelDef.getModelName().toLowerCase(), serviceCode);

        // register a dynamic endpoint that will return the generated service code and echo request
        endpointRegistrar.registerDynamicEndpoint(
                "/api/" + modelDef.getModelName().toLowerCase(),
                new Object() {
                    public Object handle(java.util.Map body) {
                        String svc = generatedServiceMap.getOrDefault(modelDef.getModelName().toLowerCase(), "No service code available");
                        return java.util.Map.of(
                                "message", "Dynamic endpoint for " + modelDef.getModelName() + " called",
                                "generatedService", svc,
                                "received", body
                        );
                    }
                },
                "handle"
        );

        return "API for " + modelDef.getModelName() + " generated and registered. Files written to /generated";
    }

    private String generateModel(ModelDefinition def) {
        StringBuilder sb = new StringBuilder();
        sb.append("package com.generated.models;\n\n");
        sb.append("public class ").append(def.getModelName()).append(" {\n\n");
        for (var f : def.getFields()) {
            sb.append("    private ").append(f.getType()).append(" ").append(f.getName()).append(";\n");
        }
        sb.append("\n    // getters and setters\n");
        sb.append("}\n");
        return sb.toString();
    }

    private String generateController(ModelDefinition def) {
        return String.format(
            "package com.generated.controllers;\n\n" +
            "import org.springframework.web.bind.annotation.*;\n" +
            "import com.generated.services.%sService;\n" +
            "import com.generated.models.%s;\n\n" +
            "@RestController\n" +
            "@RequestMapping("/api/%s")\n" +
            "public class %sController {\n\n" +
            "    private final %sService service;\n\n" +
            "    public %sController(%sService service) { this.service = service; }\n\n" +
            "    @PostMapping\n" +
            "    public Object create(@RequestBody %s body) { return service.create(body); }\n\n" +
            "}",
            def.getModelName(), def.getModelName(), def.getModelName().toLowerCase(),
            def.getModelName(), def.getModelName(), def.getModelName(),
            def.getModelName(), def.getModelName()
        );
    }
}
