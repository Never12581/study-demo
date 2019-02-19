package com.miya.huihua.hystrixdemo;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 16:16 2019/1/9
 */
public class MAIN {

    public static void main(String[] args) {
        Thread.currentThread().interrupt();
        System.out.println("是否停止1？="+Thread.interrupted());
        System.out.println("是否停止？="+Thread.interrupted());
        System.out.println("end!");
    }

}
