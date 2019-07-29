package com.hzx.monitor.service;

import com.hzx.monitor.bean.ResourceInfo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:19 2019/7/27
 * 存放监控资源
 */
public interface MonitorManager {

    /**
     * 全局唯一的变量存放的资源统计
     */
    Map<String, ResourceInfo> RESOURCE = new ConcurrentHashMap<>(2);

    /**
     * 默认资源名称
     */
    String DEFAULT_RESOURCE_NAME = "default";

    /**
     * 默认发送异常通知时间 30 min
     */
    int DEFAULT_INTERVAL_SECONDS = 30 * 60;

    /**
     * 异常日志分割符
     */
    String DELIMITER = "=========================";

}
