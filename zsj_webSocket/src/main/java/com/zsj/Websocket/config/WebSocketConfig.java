package com.zsj.Websocket.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/8.
 * 此配置类用来扫描带有@ServerEndpoint注解的Bean
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

}
