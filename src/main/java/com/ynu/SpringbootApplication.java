package com.ynu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SpringbootApplication {
    public static void main(String[] args) {
        // 启动 Spring Boot 应用
        SpringApplication.run(SpringbootApplication.class, args);
    }
}