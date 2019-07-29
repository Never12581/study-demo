package com.hzx.monitor.annoation;

import java.lang.annotation.*;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 13:23 2019/7/27
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorResource {

    /**
     * 资源名称
     *
     * @return
     */
    String resourceName() default "";

    /**
     * 时间间隔
     *
     * @return
     */
    long intervalSecords() default 30 * 60;

    /**
     * 异常阈值
     *
     * @return
     */
    int exceptionThreshold() default 5;

    /**
     * 进行监控的异常数组
     * 如果为空，则记录所有的异常
     *
     * @return
     */
    Class[] exceptions() default {};

}
