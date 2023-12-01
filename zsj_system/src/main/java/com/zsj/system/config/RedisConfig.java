package com.zsj.system.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * @author https://gitee.com/zhengshengjun
 * @date 2023/12/1.
 */
@Configuration
public class RedisConfig {


    @Value("${spring.redis.host}")
    String address;

    @Value("${spring.redis.port}")
    String port;

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RedissonClient redisClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://" + address + ":" + port);
        return Redisson.create(config);
    }

}
