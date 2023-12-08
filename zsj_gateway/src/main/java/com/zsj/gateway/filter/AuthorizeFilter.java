package com.zsj.gateway.filter;

import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.util.internal.StringUtil;
import com.zsj.common.utils.*;
import com.zsj.common.vo.LogEntity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
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

    private static final String SYSTEM_API_AUTHORIZE_NAME = "system_api_Authorize_name";

    private static final String SYSTEM_API_AUTHORIZE = "system_api_Authorize";

    @Autowired
    @Lazy
    private RedisTemplate<String, String> redisTemplate;


    @Autowired
    @Lazy
    private RedissonClient redissonClient;


    @Autowired
    RabbitTemplate rabbitTemplate;


    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        RSemaphore semaphore = redissonClient.getSemaphore("GLOBAL_REQUEST_FLOW_RATE");
        ServerHttpRequest request = exchange.getRequest();
        ServerHttpResponse response = exchange.getResponse();
        if (semaphore.tryAcquire(1)) {
            try {
                String path = request.getURI().getPath();
                String ipAddress = IPUtil.getRealIpAddress(request);
                MultiValueMap<String, String> queryParams = request.getQueryParams();
                String params = GsonUtil.gson.toJson(queryParams);
                String token_name = request.getHeaders().getFirst(SYSTEM_API_AUTHORIZE_NAME);
                String token = request.getHeaders().getFirst(SYSTEM_API_AUTHORIZE);
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
                String upgrade = request.getHeaders().getUpgrade();
                if (ObjectUtil.isNotNullOrEmpty(upgrade) && upgrade.equals("websocket")) return chain.filter(exchange);
                //特别的接口 特别放行
                if (path.contains("login") || path.contains("captcha/get")
                        || path.contains("user/pushEmailLoginCode")
                        || path.contains("emailLogin") ||
                        path.contains("qrcode/check")) {
                    //登录验证码接口放行
                    return chain.filter(exchange);
                }
                ValueOperations<String, String> ops = redisTemplate.opsForValue();

                //用户信息请求
                if (path.contains("user/info") && !StringUtil.isNullOrEmpty(token)) {
                    //如果是获取用户信息的请求 只需要看有没有token就行了
                    String sign = ops.get(token);//这个密钥是否为空
                    if (ObjectUtil.isNullOrEmpty(sign)) {
                        return response.writeWith(Flux.just(getDataBuffer(response)));
                    }
                    return chain.filter(exchange);//放行 说明没有问题
                }
                if (StringUtil.isNullOrEmpty(token_name) || StringUtil.isNullOrEmpty(token)) {
                    return response.writeWith(Flux.just(getDataBuffer(response)));
                }
                //说明有密钥 那我们就查
                String get_token = ops.get(token_name);
                //token不存在或者token不正确都不给返回
                if (StringUtil.isNullOrEmpty(get_token) || !token.equals(get_token)) {
                    return response.writeWith(Flux.just(getDataBuffer(response)));
                }
                return chain.filter(exchange);
            } finally {
                //业务执行完成之后释放信号量
                semaphore.release(1);
            }
        } else return response.writeWith(Flux.just(excessivePressure(response)));
    }

    @NotNull
    private static DataBuffer getDataBuffer(ServerHttpResponse response) {
        //封装错误返回体
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        String rs = GsonUtil.gson.toJson(R.No_auth());
        return response.bufferFactory().wrap(rs.getBytes());
    }

    @NotNull
    private static DataBuffer excessivePressure(ServerHttpResponse response) {
        //封装拒绝返回体
        response.setStatusCode(HttpStatus.FORBIDDEN);
        String rs = GsonUtil.gson.toJson(R.reject());
        return response.bufferFactory().wrap(rs.getBytes());
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
