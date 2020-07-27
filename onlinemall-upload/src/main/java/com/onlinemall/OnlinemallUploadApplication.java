package com.onlinemall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OnlinemallUploadApplication {
    public static void main(String[] args) {
        SpringApplication.run(OnlinemallUploadApplication.class,args);
    }
}
