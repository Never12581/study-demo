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

    public static void main(String[] args)  {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Future f = executorService.submit(new MyThread());

        try {
            f.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        executorService.shutdown();
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

    private static class MyThread implements Runnable {

        @Override
        public void run() {
            throw new RuntimeException("我草");
            // System.out.println("hhahaha");
        }
    }

}
