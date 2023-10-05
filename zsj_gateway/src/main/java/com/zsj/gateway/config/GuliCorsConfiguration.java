package com.zsj.gateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/9/9.
 */
@Configuration
public class GuliCorsConfiguration {

    /**
     * 进行跨域配置
     */
    @Bean
    public CorsWebFilter corsWebFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");//请求头
        corsConfiguration.addAllowedMethod("*");//请求方法
        corsConfiguration.addAllowedOrigin("*");//任意来源
        corsConfiguration.setAllowCredentials(true);//是否允许携带cookie进行跨域
        source.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsWebFilter(source);
    }
}


