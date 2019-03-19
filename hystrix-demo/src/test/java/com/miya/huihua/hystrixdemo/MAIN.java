package com.miya.huihua.hystrixdemo;


/**
 * @Author: bocai.huang
 * @Descripition: 哎
 * @Date: Create in 16:16 2019/1/9
 */
public class MAIN {

    private static Long base = 1L;

    private static final sun.misc.Unsafe UNSAFE;
    private static Long BASE ;

    static {
        try {
            UNSAFE = sun.misc.Unsafe.getUnsafe();
            Class<?> clazz = MAIN.class;
            BASE = UNSAFE.objectFieldOffset(clazz.getField("base"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }


    public static void main(String[] args) {
        System.out.println(BASE);
        System.out.println(base++);
        System.out.println(BASE);
    }

}
