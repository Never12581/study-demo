package com.hzx.juc.threadlocal;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:34 2019/8/28
 */
public class TConstants {

    private final static ThreadLocal<String> t = new ThreadLocal<>();

    public static void set(String s) {
        t.set(s);
    }

    public static String get(){
        return t.get();
    }

    public static void clear(){
        t.remove();
    }

}
