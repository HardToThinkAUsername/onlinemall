package com.onlinemall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class OnlinemallRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlinemallRegistryApplication.class, args);
    }

}
