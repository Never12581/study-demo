package com.hzx.sc.config;

import com.hzx.sc.schedule.TestTimeTask;
import com.hzx.sc.service.TestService;
import com.hzx.sc.service.TestServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: bocai.huang
 * @create: 2019-11-08 17:47
 **/
@Configuration
@ConditionalOnBean(value = {TestServiceImpl.class})
public class ScConfig {

    @Bean
    public TestTimeTask testTimeTask(){
        return new TestTimeTask();
    }

}
