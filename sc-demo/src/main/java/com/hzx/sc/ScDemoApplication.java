package com.hzx.sc;

import com.hzx.sc.schedule.TestTimeTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class ScDemoApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(ScDemoApplication.class, args);
        TestTimeTask  testTimeTask = context.getBean(TestTimeTask.class);
        System.out.println(testTimeTask == null);
    }

}
