package com.hzx.juc.threadlocal.wyj;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 23:19 2019/11/13
 */
public class TContext {
    private final static ThreadLocal<AtomicInteger> t = new InheritableThreadLocal<>();

    public static void set(AtomicInteger s) {
        t.set(s);
    }

    public static AtomicInteger get(){
        return t.get();
    }

    public static void clear(){
        t.remove();
    }

}
