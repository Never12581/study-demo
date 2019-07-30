package com.hzx.sc;

import com.alibaba.fastjson.JSON;
import com.hzx.sc.rest.TestController;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
@EnableFeignClients
public class ScDemoApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ScDemoApplication.class, args);
        TestController testController = context.getBean(TestController.class);
        System.out.println(JSON.toJSONString(testController));
    }

}
