package com.autogen.apicreator.service;

import com.autogen.apicreator.model.ModelDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

// NOTE: This implementation is a safe placeholder that creates compilable Java service stubs.
// Replace or extend with real OpenAI/LLM client calls if you provide an API key.
@Service
public class AIAgentService {

    @Value("${openai.api.key:}")
    private String openAiKey;

    public String generateBusinessLogic(ModelDefinition modelDef) {
        String className = modelDef.getModelName() + "Service";
        String model = modelDef.getModelName();
        String logic = modelDef.getBusinessLogic() == null ? "" : modelDef.getBusinessLogic();

        // Simple heuristic: if request contains 'discount' or 'apply' try to generate a sample method.
        if (logic.toLowerCase().contains("discount")) {
            return String.format(
                "package com.generated.services;\n\n" +
                "import com.generated.models.%s;\n" +
                "import org.springframework.stereotype.Service;\n\n" +
                "@Service\n" +
                "public class %s {\n\n" +
                "    public %s create(%s body) {\n" +
                "        // generated business logic: sample discount application\n" +
                "        try {\n" +
                "            java.lang.reflect.Method m = %s.class.getMethod(\"getAmount\");\n" +
                "        } catch (Exception e) {}\n" +
                "        return body;\n" +
                "    }\n\n" +
                "    public double calculateFinalAmount(%s body) {\n" +
                "        try {\n" +
                "            java.lang.reflect.Method m = %s.class.getMethod(\"getAmount\");\n" +
                "        } catch (Exception e) {}\n" +
                "        return 0.0;\n" +
                "    }\n" +
                "}\", model, className, model, model, model, model
            );
        }

        // default stub
        return String.format(
            "package com.generated.services;\n\n" +
            "import com.generated.models.%s;\n" +
            "import org.springframework.stereotype.Service;\n\n" +
            "@Service\n" +
            "public class %s {\n\n" +
            "    public %s create(%s body) {\n" +
            "        // placeholder generated business logic: %s\n" +
            "        return body;\n" +
            "    }\n\n" +
            "}\", model, className, model, model, logic.replaceAll("\"", "\\\"") 
        );
    }
}
