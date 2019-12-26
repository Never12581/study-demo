package com.hzx.dubbo;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:45 2019/12/25
 */
@SpringBootApplication
@EnableDubbo
public class TempProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(TempProviderApplication.class, args);
    }

}
