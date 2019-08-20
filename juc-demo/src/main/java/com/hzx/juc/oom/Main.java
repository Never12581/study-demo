package com.hzx.juc.oom;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 23:30 2019/8/20
 */
public class Main {

    public static void main(String[] args) {
        // t();
        // f();
        permSizeTest();
    }

    /**
     * 老年代测试
     */
    public static void permSizeTest(){
        int i = 0 ;
        try {
            String s = "";
            while (true) {
                s = s + i;
                s.intern();
                i++;
            }
        } catch (Error error){
            System.out.println("i:"+i);
            error.printStackTrace();
        }
    }
    /**
     * 方法嵌套过多
     */
    public static void t() {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName());
        });
        t();
    }

    /**
     * 多线程，线程对象过多
     */
    public static void f() {
        while (true) {
            new Thread(() -> {
                while (true) {
                    // System.out.println(Thread.currentThread().getName());
                    // try {
                    //     Thread.sleep(100000L);
                    // } catch (InterruptedException e) {
                    //     e.printStackTrace();
                    // }
                    // p(Thread.currentThread());
                }

            }).start();
        }
    }

    public static void p(Thread thread) {
        System.out.println(thread.getName());
    }

}
