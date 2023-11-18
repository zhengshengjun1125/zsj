package com.zsj.sms.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.commons.util.InetUtils;
import org.springframework.cloud.commons.util.InetUtilsProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/11/17.
 */
@Configuration
public class InetUtilsConfig {

    @Bean
    @ConditionalOnMissingBean
    @Primary
    public InetUtils InetUtils() {
        return new InetUtils(new InetUtilsProperties());
    }

    @Bean
    @ConditionalOnMissingBean
    @Primary
    public InetUtilsProperties InetUtilsProperties() {
        return new InetUtilsProperties();
    }

}
