package com.hzx.monitor.config;

import com.hzx.monitor.annoation.EnableMonitor;
import com.hzx.monitor.service.MonitorExecuteAspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 15:17 2019/7/27
 */
@ConditionalOnBean(annotation = EnableMonitor.class)
@Component
public class AspectConfig {

    @Bean
    public MonitorExecuteAspect monitorExecuteAspect() {
        return new MonitorExecuteAspect();
    }

}
