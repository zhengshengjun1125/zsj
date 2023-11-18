package com.zsj.Websocket.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;
import java.util.List;
import java.util.Map;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/8.
 * 获取httpsession对象
 */
@Slf4j
public class HttpSessionConfigurator extends ServerEndpointConfig.Configurator {
    public static final String CUR_LOGIN_USER = "CUR_LOGIN_USER";

    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        HttpSession httpSession = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), httpSession);
//        Map<String, List<String>> headers = request.getHeaders();
//        String name = headers.get("system_api_Authorize_name").get(0);
//        log.info("the websocket module get request header name is {}", name);
//        sec.getUserProperties().put(CUR_LOGIN_USER, name);
    }

}
