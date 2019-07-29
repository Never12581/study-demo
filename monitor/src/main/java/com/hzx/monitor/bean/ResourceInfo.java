package com.hzx.monitor.bean;

import com.hzx.monitor.annoation.MonitorResource;
import com.hzx.monitor.config.MonitorConfig;
import com.hzx.monitor.service.MonitorManager;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:48 2019/7/27
 * 用于存储
 * 资源名称
 * 异常次数
 * 异常信息
 * 启始时间
 */
public class ResourceInfo implements Serializable {

    /**
     * 资源名称
     */
    private String resourceName;
    /**
     * 资源异常次数
     */
    private LongAdder exceptionCount;
    /**
     * 资源异常阈值
     */
    private int exceptionThreshold;
    /**
     * 资源异常堆栈信息
     */
    private StringBuilder exceptionInfo;
    /**
     * 当前对象存在的启始时间
     */
    private long startTime;

    /**
     * 本资源统计的时间间隔
     */
    private long intervalSecords;

    public ResourceInfo() {
        resourceName = MonitorManager.DEFAULT_RESOURCE_NAME;

        exceptionCount = new LongAdder();
        exceptionThreshold = MonitorConfig.getDefaultExceptionThreshold();

        exceptionInfo = new StringBuilder();
        startTime = System.currentTimeMillis();
        intervalSecords = MonitorConfig.getDefaultIntervalSeconds();
    }

    public ResourceInfo(MonitorResource monitorResource) {
        exceptionCount = new LongAdder();
        exceptionInfo = new StringBuilder();
        startTime = System.currentTimeMillis();

        if (monitorResource == null) {
            resourceName = MonitorManager.DEFAULT_RESOURCE_NAME;
            intervalSecords = MonitorManager.DEFAULT_INTERVAL_SECONDS;
        } else {
            resourceName = monitorResource.resourceName();
            if (StringUtils.isBlank(resourceName)) {
                resourceName = MonitorManager.DEFAULT_RESOURCE_NAME;
            }
            intervalSecords = monitorResource.intervalSecords();
            if (intervalSecords <= 0) {
                intervalSecords = MonitorConfig.getDefaultIntervalSeconds();
            }
            exceptionThreshold = monitorResource.exceptionThreshold();
            if (exceptionThreshold <= 0) {
                exceptionThreshold = MonitorConfig.getDefaultExceptionThreshold();
            }
        }
    }

    public String getResourceName() {
        return resourceName;
    }

    public ResourceInfo setResourceName(String resourceName) {
        this.resourceName = resourceName;
        return this;
    }

    public LongAdder getExceptionCount() {
        return exceptionCount;
    }

    public ResourceInfo setExceptionCount(LongAdder exceptionCount) {
        this.exceptionCount = exceptionCount;
        return this;
    }

    public StringBuilder getExceptionInfo() {
        return exceptionInfo;
    }

    public ResourceInfo setExceptionInfo(StringBuilder exceptionInfo) {
        this.exceptionInfo = exceptionInfo;
        return this;
    }

    public long getStartTime() {
        return startTime;
    }

    public ResourceInfo setStartTime(long startTime) {
        this.startTime = startTime;
        return this;
    }
}
