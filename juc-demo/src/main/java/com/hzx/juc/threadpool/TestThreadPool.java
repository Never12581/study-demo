package com.hzx.juc.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:10 2019/4/15
 */
public class TestThreadPool {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // test();
        System.out.println((1 <<29)-1);
    }

    /**
     * 证明：线程被恶意中断，依旧会存在于线程池中
     */
    public static void test() throws InterruptedException, ExecutionException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        Future<String> future = executorService.submit(() -> {
            System.out.println(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getName());
            // try {
            //     Thread.sleep(1000);
            // } catch (InterruptedException e) {
            //
            // }

            // Thread.currentThread().stop();

            // if (1 == 1) {
            //     throw new RuntimeException("故意的");
            // }

            return "hahah";
        });

        future.get();

        Future<String> s1 = executorService.submit(() -> {

            System.out.println(Thread.currentThread().getId());
            System.out.println(Thread.currentThread().getName());

            // Thread.sleep(1000);

            return " heheh ";
        });

        Thread.sleep(1000);

        executorService.shutdown();
    }

}
