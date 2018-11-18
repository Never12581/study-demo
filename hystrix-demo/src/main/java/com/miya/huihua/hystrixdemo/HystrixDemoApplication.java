package com.miya.huihua.hystrixdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;

@SpringBootApplication
@EnableCircuitBreaker
public class HystrixDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(HystrixDemoApplication.class, args);
    }
}
