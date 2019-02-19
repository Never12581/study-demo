package com.miya.huihua.hystrixdemo;

import java.util.concurrent.*;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:30 2018/11/22
 */
public class TestMain {
    // hystrix 线程池
    static ExecutorService hystrixThreadPool = Executors.newSingleThreadExecutor();
    // fallback 线程池
    static ExecutorService fallbackThreadPool = Executors.newSingleThreadExecutor();

    public static void main(String[] args) {
        // 超时时间
        long timeout = 50;
        Future future = hystrixThreadPool.submit(() -> {
            // 模拟任务
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        try {
            future.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            // 当前仅模拟超时情况
            // 正常流程线程取消
            // ps:cancel这个方法可能会取消，意指 如果已完成则取消失败返回false，如果线程未开始调用该方法则调用成功返回true，
            // 如果线程已开始未完成则根据mayInterruptIfRunning 来判断是否取消该线程任务。
            future.cancel(true);
            // 执行fallback线程
            fallbackThreadPool.submit(() -> {
                // do something fallback
            });
        }
    }
}
