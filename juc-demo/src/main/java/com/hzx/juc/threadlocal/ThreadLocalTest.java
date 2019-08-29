package com.hzx.juc.threadlocal;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:36 2019/8/28
 */
public class ThreadLocalTest {

    public static void main(String[] args) throws InterruptedException {
        test3();
    }

    public static void test1() {
        System.out.println(Thread.currentThread().getName());
        String s = "aaa";
        new Thread(() -> {
            TConstants.set(s);
            System.out.println(Thread.currentThread().getName() + "---------" + TConstants.get());
            TConstants.clear();
        }).start();
    }

    public static void test2() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);

        List<String> list = new ArrayList<>(1024);

        for (int i = 0; i < 1024; i++) {
            list.add("a" + i);
        }

        es.execute(() -> {
            SoftReference<String> sstr = new SoftReference<>("str");
            WeakReference<String> wstr = new WeakReference<>("str");
            String s = "abc";
            TConstants.set(s);
            print("---------" + TConstants.get());

            try {
                Thread.sleep(10000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            print("sstr:" + sstr.get());
            print("wstr:" + wstr.get());

        });

        System.out.println("start gc");
        for (int i = 0; i < 999; i++) {
            System.gc();
        }
        System.out.println("end gc");

        Thread.sleep(1000L);

        es.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " 在同一个线程另一个任务里 ============ " + TConstants.get());
        });
        es.shutdown();
    }

    public static void test3() {
        Integer a = 3;

        IntegerConstants.set(a);

        a = 4;

        aaa();

        System.out.println("main "+a);
    }

    private static void aaa() {
        System.out.println("int aaa "+ IntegerConstants.get());
    }

    public static void print(Object object) {
        System.out.println(Thread.currentThread().getName() + "=========" + object.toString());
    }

}
