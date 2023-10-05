package com.zsj.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZsjGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZsjGatewayApplication.class, args);
    }

}
