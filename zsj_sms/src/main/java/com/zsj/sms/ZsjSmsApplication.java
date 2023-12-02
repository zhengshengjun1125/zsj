package com.zsj.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableRabbit
@EnableDiscoveryClient
@MapperScan(basePackages = "com.zsj.sms.dao")
@EnableFeignClients
public class ZsjSmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZsjSmsApplication.class, args);
    }

}
