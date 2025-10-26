package com.autogen.apicreator.model;

import lombok.Data;
import java.util.List;

@Data
public class ModelDefinition {
    private String modelName;
    private List<Field> fields;
    private List<String> operations;
    private String businessLogic;

    @Data
    public static class Field {
        private String name;
        private String type;
    }
}
