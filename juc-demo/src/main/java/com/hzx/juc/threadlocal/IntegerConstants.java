package com.hzx.juc.threadlocal;

/**
 * @author: bocai.huang
 * @create: 2019-08-29 17:56
 **/
public class IntegerConstants {

    private final static ThreadLocal<Integer> t = new ThreadLocal<>();

    public static void set(Integer s) {
        t.set(s);
    }

    public static Integer get() {
        return t.get();
    }

    public static void clear() {
        t.remove();
    }


}
