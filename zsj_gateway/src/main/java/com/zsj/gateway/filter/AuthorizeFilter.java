package com.zsj.gateway.filter;

import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import com.zsj.common.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
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

        //接口测试
        return chain.filter(exchange);

        //是否登录的鉴权

//        if (path.contains("login") || path.contains("captcha/get")
//                || path.contains("user/pushEmailLoginCode")
//                || path.contains("emailLogin") ||
//                path.contains("qrcode/check")) {
//            //登录验证码接口放行
//            return chain.filter(exchange);
//        }
//        ValueOperations<String, String> ops = redisTemplate.opsForValue();
//        //用户信息请求
//
//        if (path.contains("user/info") && !StringUtil.isNullOrEmpty(token)) {
//            //如果是获取用户信息的请求 只需要看有没有token就行了
//            //根据这个token键去获取用户信息  拿得到说明登录成功 拿不到就说明是非法操作
//            String json = ops.get(token);//这个json是否为空
//            if (ObjectUtil.isNullOrEmpty(json)) {
//                return response.writeWith(Flux.just(getDataBuffer(response)));
//            }
//            return chain.filter(exchange);//放行 说明没有问题
//        }
//        if (StringUtil.isNullOrEmpty(token_name) || StringUtil.isNullOrEmpty(token)) {
//            return response.writeWith(Flux.just(getDataBuffer(response)));
//        }
//        //说明有密钥 那我们就查
//        String get_token = ops.get(token_name);
//        //token不存在或者token不正确都不给返回
//        if (StringUtil.isNullOrEmpty(get_token) || !token.equals(get_token)) {
//            return response.writeWith(Flux.just(getDataBuffer(response)));
//        }
//        return chain.filter(exchange);
    }

    @NotNull
    private static DataBuffer getDataBuffer(ServerHttpResponse response) {
        //封装错误返回体
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String rs = GsonUtil.gson.toJson(R.No_auth());
        return response.bufferFactory().wrap(rs.getBytes());
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
