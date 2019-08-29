package com.hzx.juc.threadlocal;

import java.lang.ref.WeakReference;

public class WeakRefrenceDemo {

    public static void main(String[] args) {
        // TestUser user = new TestUser("hello", "123");
        WeakReference<TestUser> userWeakReference = new WeakReference<>(new TestUser("hello", "123"));
        System.out.println(userWeakReference.get());
        //另一种方式触发GC，强制执行GC
        System.gc();
        System.runFinalization();
        System.out.println(userWeakReference.get());
    }


}