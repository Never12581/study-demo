package com.hzx.juc.unsafe;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 10:27 2019/4/2
 */
public class UnsafeTest {

    private static final long BASE;
    private static final long SECOND;
    private static final Unsafe U;

    static {
        try {
            Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
            theUnsafe.setAccessible(true);
            U = (Unsafe) theUnsafe.get(null);

            Class<UnsafeTest> u = UnsafeTest.class;
            BASE = U.objectFieldOffset(u.getDeclaredField("base"));
            SECOND = U.objectFieldOffset(u.getDeclaredField("second"));

        } catch (Exception e) {
            throw new Error(e);
        }

    }

    private volatile long base = 0;

    private volatile long second = 1 ;

    public static void main(String[] args) {
        System.out.println(BASE);
        System.out.println(SECOND);

    }

    public void test(long before, long after) {
        boolean b = U.compareAndSwapLong(this, BASE, before, after);
        System.out.println(b);
        System.out.println(BASE);
        System.out.println(base);
        Long k = U.getLongVolatile(this, BASE);
        System.out.println(k);
    }

}
