package com.zsj.gateway.filter;

import com.google.gson.Gson;
import com.zsj.common.utils.GsonUtil;
import com.zsj.common.utils.R;
import io.netty.util.internal.StringUtil;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange);
        //todo  系统开发完成后 打开以下注解 来进行登录鉴权
        //
//        ServerHttpRequest request = exchange.getRequest();
//        ServerHttpResponse response = exchange.getResponse();
//        String path = request.getURI().getPath();
//        if (path.contains("login")) {
//            //登录接口放行
//            return chain.filter(exchange);
//        }
//
//        String token_name = request.getHeaders().getFirst("system_api_Authorize_username");
//        String token= request.getHeaders().getFirst("system_api_Authorize");
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
//
//        //token不存在或者token不正确都不给返回
//        if (StringUtil.isNullOrEmpty(get_token) || !token.equals(get_token)){
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            String json = GsonUtil.gson.toJson(R.No_auth());
//            DataBuffer wrap = response.bufferFactory().wrap(json.getBytes());
//            return response.writeWith(Flux.just(wrap));
//        }
//
//        return chain.filter(exchange);
    }


    @Override
    public int getOrder() {
        return 0;
    }
}
