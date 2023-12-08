package com.zsj.gateway.config;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class RedisConfig {


    @Value("${spring.redis.host}")
    String address;

    @Value("${spring.redis.port}")
    String port;

    @Value("${spring.profiles.active}")
    String active;

    @Bean
    @Primary
    @ConditionalOnMissingBean
    public RedissonClient redissonClient() {
        Config config = new Config();
        log.info("host is {}", address);
        log.info("client redis active is {}", active);
        if (active.equals("prod")) {
            config.useSingleServer().setAddress("redis://" + address + ":" + port).setPassword("zsjiscoder");
        } else {
            config.useSingleServer().setAddress("redis://" + address + ":" + port);
        }
        return Redisson.create(config);
    }

}
