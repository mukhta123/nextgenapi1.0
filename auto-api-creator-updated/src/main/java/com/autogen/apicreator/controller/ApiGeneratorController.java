package com.autogen.apicreator.controller;

import com.autogen.apicreator.model.ModelDefinition;
import com.autogen.apicreator.service.CodeGeneratorService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/generate")
@CrossOrigin(origins = "*")
public class ApiGeneratorController {

    private final CodeGeneratorService codeGeneratorService;

    public ApiGeneratorController(CodeGeneratorService codeGeneratorService) {
        this.codeGeneratorService = codeGeneratorService;
    }

    @PostMapping
    public String generateApi(@RequestBody ModelDefinition modelDef) {
        return codeGeneratorService.generateApi(modelDef);
    }
}
