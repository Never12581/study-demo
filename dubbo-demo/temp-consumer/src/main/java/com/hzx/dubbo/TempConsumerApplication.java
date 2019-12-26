package com.hzx.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:43 2019/12/25
 */
@SpringBootApplication
@EnableDubbo
public class TempConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempConsumerApplication.class, args);
    }

}
