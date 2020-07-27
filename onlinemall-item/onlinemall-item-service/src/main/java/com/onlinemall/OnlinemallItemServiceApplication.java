package com.onlinemall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
public class OnlinemallItemServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OnlinemallItemServiceApplication.class, args);
    }
}