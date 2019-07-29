package com.hzx.monitor.config;

import com.hzx.monitor.annoation.EnableMonitor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 11:48 2019/7/27
 */
@Configuration
@ConfigurationProperties(prefix = "hzx.monitor")
@ConditionalOnBean(annotation = EnableMonitor.class)
public class MonitorConfig implements InitializingBean {

    /**
     * 默认时间间隔
     */
    private static long DEFAULT_INTERVAL_SECONDS = 30 * 60;

    /**
     * 默认异常阈值
     */
    private static int DEFAULT_EXCEPTION_THRESHOLD = 5;

    /**
     * 时间间隔
     */
    private long intervalSeconds;

    /**
     * 单时间间隔内异常次数
     */
    private int exceptionThreshold;

    public static long getDefaultIntervalSeconds() {
        return DEFAULT_INTERVAL_SECONDS;
    }

    public static int getDefaultExceptionThreshold() {
        return DEFAULT_EXCEPTION_THRESHOLD;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (intervalSeconds > 0) {
            DEFAULT_INTERVAL_SECONDS = intervalSeconds;
        }
        if (exceptionThreshold > 0) {
            DEFAULT_EXCEPTION_THRESHOLD = exceptionThreshold;
        }
    }

}
