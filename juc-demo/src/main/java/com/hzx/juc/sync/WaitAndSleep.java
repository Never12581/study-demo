package com.hzx.juc.sync;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 10:19 2019/3/20
 */
public class WaitAndSleep {

    private static int i = 200;

    /**
     * 得出结论，wait会释放锁释放cpu，但是wait以后不会主动去获取锁！
     * sleep会释放cpu，但是并不会释放锁
     * notify 会唤醒其他线程去竞争锁，但是要在synchronized代码块执行完毕以后，也就是当前线程释放锁以后
     *
     * @param args
     */
    public static void main(String[] args) {
        test3();
    }

    public static void test() {
        Object o = new Object();
        Thread t1 = new Thread(() -> {
            synchronized (o) {
                try {
                    while (i >= 0) {
                        if (i % 5 == 0) {
                            System.out.println(Thread.currentThread().getName() + " 此时是5的倍数 i:" + i);
                            o.wait();
                        }
                        o.notify();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (o) {
                try {
                    while (i >= 0) {
                        i--;
                        if (i % 5 != 0) {
                            System.out.println(Thread.currentThread().getName() + " 此时不是5的倍数 i:" + i);
                        } else {
                            o.wait();
                        }
                        o.notify();

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        t1.start();
        t2.start();
    }

    public static void test1() {
        Object o = new Object();
        Thread t1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "已经开始了哈");
            synchronized (o) {
                try {
                    while (i >= 0) {
                        if (i % 3 == 0 || i == 200) {
                            System.out.println(Thread.currentThread().getName() + " 此时是3的倍数 i:" + i);
                            o.wait();
                        }
                        o.notify();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "已经开始了哈哈");

            synchronized (o) {
                try {
                    while (i >= 0) {
                        i--;
                        if (i % 3 != 0) {
                            System.out.println(Thread.currentThread().getName() + " 此时不是3的倍数 i:" + i);
                        } else {
                            o.wait();
                        }
                        o.notify();

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        Thread t3 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "已经开始了哈哈哈");

            synchronized (o) {
                if (i == 200) {
                    System.out.println(Thread.currentThread().getName() + "此时，i：" + i);
                } else {
                    System.out.println(Thread.currentThread().getName() + "此时 【可惜了】，i：" + i);
                }
                o.notifyAll();
            }
        });

        t1.start();
        t2.start();
        t3.start();
    }

    public static void test2(){
        new Thread(()->{
            long start = System.currentTimeMillis();
            for(int i = 0 ; i < 10000 ; i++) {
                System.out.println("我获取了时间片");
                Thread.yield();
            }
            System.out.println("使用了："+(System.currentTimeMillis() - start) );
        }).start();
    }

    public static void  test3(){
        Map<Object,Object> map = new HashMap<>();
        Object o = map.put(1,1);
        System.out.println(o);
        Object o1 = map.put(1,1);
        System.out.println(o1);


        // System.out.println(4>>1);
        // System.out.println(-4>>>1);//2147483647
        // System.out.println(-4>>1);

    }

}
