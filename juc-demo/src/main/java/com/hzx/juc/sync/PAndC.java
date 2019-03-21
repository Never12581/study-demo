package com.hzx.juc.sync;

/**
 * @Author: bocai.huang
 * @Descripition: 生产者消费者问题
 * @Date: Create in 14:47 2019/3/20
 */
public class PAndC {

    volatile static String value = "";

    public static void main(String[] args) {

        Object lock = new Object();

        new Thread(() -> {
            try {
                while (true) {
                    synchronized (lock) {
                        if (value.equals("")) {
                            lock.wait();
                        }else {
                            System.out.println(Thread.currentThread().getName() + " 【get】 value: " + value);
                            value = "";
                        }
                        lock.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        // new Thread(() -> {
        //     try {
        //         while (true) {
        //             synchronized (lock) {
        //                 if (value.equals("")) {
        //                     lock.wait();
        //                 }else {
        //                     System.out.println(Thread.currentThread().getName() + " 【get】 value: " + value);
        //                     value = "";
        //                 }
        //                 lock.notify();
        //             }
        //         }
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }).start();
        // new Thread(() -> {
        //     try {
        //         while (true) {
        //             synchronized (lock) {
        //                 if (value.equals("")) {
        //                     lock.wait();
        //                 }else {
        //                     System.out.println(Thread.currentThread().getName() + " 【get】 value: " + value);
        //                     value = "";
        //                 }
        //                 lock.notify();
        //             }
        //         }
        //     } catch (InterruptedException e) {
        //         e.printStackTrace();
        //     }
        // }).start();

        new Thread(() -> {
            try {
                while (true) {
                    synchronized (lock) {
                        if(!value.equals("")) {
                            lock.wait();
                        }
                        value = System.currentTimeMillis()+"";
                        System.out.println(Thread.currentThread().getName() + " 【set】 value: " + value);
                        lock.notifyAll();
                        // Thread.yield();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            try {
                while (true) {
                    synchronized (lock) {
                        if(!value.equals("")) {
                            lock.wait();
                        }
                        value = System.currentTimeMillis()+"";
                        System.out.println(Thread.currentThread().getName() + " 【set】 value: " + value);
                        lock.notifyAll();
                        // Thread.yield();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                while (true) {
                    synchronized (lock) {
                        if(!value.equals("")) {
                            lock.wait();
                        }
                        value = System.currentTimeMillis()+"";
                        System.out.println(Thread.currentThread().getName() + " 【set】 value: " + value);
                        lock.notifyAll();
                        // Thread.yield();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

    }

}
