package com.hzx.juc.threadlocal.wyj;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 23:21 2019/11/13
 */
public class Main {

    public static void main(String[] args) {
        // 当作父线程
        new Thread(() -> {
            AtomicInteger ac = new AtomicInteger(0);
            TContext.set(ac);

            while (true) {
                System.out.println("父"+Thread.currentThread().getName()+" ac:"+ac.get());
                if (ac.get() > 3) {
                    break;
                } else {
                    // 当作子线程
                    new Thread(()->{

                        // 从 父 线程中，获取当前 的 AtomicInteger 对象
                        AtomicInteger atomicInteger = TContext.get();

                        System.out.println(ac == atomicInteger);
                        System.out.println(Thread.currentThread().getName()+"-----"+atomicInteger.get());
                        // 自增1
                        atomicInteger.incrementAndGet();
                    }).start();
                }

            }

            TContext.clear();

            System.out.println(ac.get());
        }).start();

    }

}
