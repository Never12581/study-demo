package com.hzx.juc.proxy.user;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:59 2019/8/26
 */
public class UserImpl implements User {
    @Override
    public void say() {
        System.out.println("hello China!");
    }
}
