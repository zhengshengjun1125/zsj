package com.zsj.gateway.filter;

import com.zsj.common.utils.GlobalValueToExchange;
import com.zsj.common.utils.GsonUtil;
import com.zsj.common.utils.IPUtil;
import com.zsj.common.utils.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/10/8.
 * <p>
 * 全局校验接口
 */

@Slf4j
@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {


    @Autowired
    @Lazy
    private RedisTemplate<String, String> redisTemplate;





    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        //todo 进行请求日志的记录 可以考虑存到es中
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        String path = request.getURI().getPath();
        String ipAddress = IPUtil.getRealIpAddress(request);
        MultiValueMap<String, String> queryParams = request.getQueryParams();
        String params = GsonUtil.gson.toJson(queryParams);
        String token_name = request.getHeaders().getFirst("system_api_Authorize_name");
        String token = request.getHeaders().getFirst("system_api_Authorize");
        String method = Objects.requireNonNull(request.getMethod()).toString();
        LogEntity logEntity = new LogEntity();
        logEntity.setIp(ipAddress);
        logEntity.setCreateDate(new Date(System.currentTimeMillis()));
        logEntity.setParams(params);
        logEntity.setMethod(method);
        if (!Objects.isNull(token_name)) logEntity.setUsername(token_name);
        logEntity.setOperation(path);
        //消息推送 此队列是点对点进行推送
        rabbitTemplate.convertAndSend(GlobalValueToExchange.LOG_EXCHANGE,
                "system.logs", logEntity, new CorrelationData(UUID.randomUUID().toString()));
        //测试功能阶段直接放行 不需要登录
        return chain.filter(exchange);


        // 是否登录的鉴权
//        if (path.contains("login") || path.contains("captcha/get")) {
//            //登录接口放行
//            return chain.filter(exchange);
//        }
//
//        if (StringUtil.isNullOrEmpty(token_name) || StringUtil.isNullOrEmpty(token)) {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            String json = GsonUtil.gson.toJson(R.No_auth());
//            DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
//            return response.writeWith(Flux.just(wrap));
//        }
//        //说明有密钥 那我们就查
//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        String get_token = ops.get(token_name);
//        //token不存在或者token不正确都不给返回
//        if (StringUtil.isNullOrEmpty(get_token) || !token.equals(get_token)) {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            String json = GsonUtil.gson.toJson(R.No_auth());
//            DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
//            return response.writeWith(Flux.just(wrap));
//        }
//        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
