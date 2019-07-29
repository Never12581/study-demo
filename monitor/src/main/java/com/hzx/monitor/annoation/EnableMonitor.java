package com.hzx.monitor.annoation;

import java.lang.annotation.*;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 13:45 2019/7/27
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableMonitor {
}
