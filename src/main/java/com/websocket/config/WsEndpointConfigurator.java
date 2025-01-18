package com.websocket.config;

import org.springframework.context.ApplicationContext;
import javax.websocket.server.ServerEndpointConfig;

public class WsEndpointConfigurator extends ServerEndpointConfig.Configurator {

    private static ApplicationContext context;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        WsEndpointConfigurator.context = applicationContext;
    }

    @Override
    public <T> T getEndpointInstance(Class<T> endpointClass) {
        return context.getBean(endpointClass);
    }

}
