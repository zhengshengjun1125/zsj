package com.zsj.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class ZsjSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(ZsjSystemApplication.class, args);
    }

}
