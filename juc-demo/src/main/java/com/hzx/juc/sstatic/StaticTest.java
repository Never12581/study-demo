package com.hzx.juc.sstatic;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:16 2019/4/18
 */
public class StaticTest {

    public static void main(String[] args) {
        // Singleton singleton = Singleton.getInstance();
        System.out.println("=========");
        System.out.println(Singleton.value1);
        System.out.println(Singleton.value2);
    }

}
