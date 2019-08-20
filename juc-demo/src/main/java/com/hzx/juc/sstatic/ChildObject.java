package com.hzx.juc.sstatic;

/**
 * @author: bocai.huang
 * @create: 2019-08-20 10:47
 **/
public class ChildObject extends ParentObject {

    public static int c2 = 9;

    private static int c0 = 0;

    static {
        c1 = 3;
        System.out.println("[child] static c0 = " + c0);
        c2--;
        System.out.println("[child] static c2 = " + c2);
    }

    private static int c1 = 1;

    static {
        System.out.println("[child] static c1 = " + c1);
    }

    {
        c1 = 0;
        c0 = 1;
        System.out.println("[child] method c1 = " + c1);
        System.out.println("[child] method c0 = " + c0);
        c2--;
        System.out.println("[child] method c2 = " + c2);
    }

}
