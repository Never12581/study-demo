package com.hzx.sc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class ScDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScDemoApplication.class, args);
    }

}
