package com.autogen.apicreator.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;

import java.lang.reflect.Method;

@Component
public class DynamicEndpointRegistrar {

    private final RequestMappingHandlerMapping handlerMapping;
    private final ApplicationContext appContext;

    @Autowired
    public DynamicEndpointRegistrar(RequestMappingHandlerMapping handlerMapping, ApplicationContext appContext) {
        this.handlerMapping = handlerMapping;
        this.appContext = appContext;
    }

    /**
     * Registers a dynamic endpoint (POST) at runtime.
     * controllerInstance must have a public method with the name methodName that accepts a Map (or Object).
     */
    public void registerDynamicEndpoint(String path, Object controllerInstance, String methodName) {
        try {
            Method method = controllerInstance.getClass().getMethod(methodName, java.util.Map.class);
            RequestMappingInfo info = RequestMappingInfo
                    .paths(path)
                    .methods(RequestMethod.POST)
                    .build();

            handlerMapping.registerMapping(info, controllerInstance, method);
        } catch (Exception e) {
            throw new RuntimeException("Failed to register dynamic endpoint: " + path, e);
        }
    }
}
