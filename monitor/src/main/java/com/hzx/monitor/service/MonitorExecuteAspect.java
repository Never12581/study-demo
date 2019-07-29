package com.hzx.monitor.service;

import com.hzx.monitor.annoation.MonitorResource;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 13:48 2019/7/27
 */
@Aspect
public class MonitorExecuteAspect {

    private final static Logger logger = LoggerFactory.getLogger(MonitorExecuteAspect.class);

    @AfterThrowing(pointcut = "@annotation(com.hzx.monitor.annoation.MonitorResource)", throwing = "exception")
    public void execute(JoinPoint point, Exception exception) {

        logger.info("查看线程是否是同一个");

        MonitorResource monitorResource = null;
        Class clazz = point.getSignature().getDeclaringType();
        String methodName = point.getSignature().getName();
        Method method = null;
        try {
            method = clazz.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            logger.error("没有那个方法!");
        }

        if (method == null) {
            return;
        }

        monitorResource = method.getAnnotation(MonitorResource.class);

        if(monitorResource == null) {
            return;
        }

        // 此处异步执行吗？进行异常信息的统计

    }

}
