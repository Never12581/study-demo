package com.hzx.juc.sstatic;

/**
 * @author: bocai.huang
 * @create: 2019-08-20 10:47
 **/
public class ParentObject {

    public static int v0 = 1;

    static {
        System.out.println("[parent]第一个静态方法块。当前 v0 = " + v0);
        v0 = v0++;
        System.out.println("[parent]第一个静态方法块。当前 v0 = " + v0);
    }

    public ParentObject() {
        System.out.println("[parent] constructor");
        v1 = 3;
    }

    private static int v1 = 1;

    {
        System.out.println("[parent] 你猜我是不是在构造方法前运行");
    }

}
