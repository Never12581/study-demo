package com.hzx.sc.config;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bocai.huang
 * @Descripition: 线程池
 * @Date: Create in 14:00 2019/4/10
 */
public interface ApplicationResource {

    ExecutorService RESOURCE = Executors.newFixedThreadPool(8);

}
