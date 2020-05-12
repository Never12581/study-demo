package com.hzx.juc.proxy.dynamic;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {
        //利用invocationHandler中定义的绑定方法实现动态代理
        IA i = (IA) IAProxy.bind(IA.class);
        System.out.println(i.paly());
        //创建代理时添加绑定关系
        IAProxy p = new IAProxy();
        IA pl = (IA) Proxy.newProxyInstance(IA.class.getClassLoader(), new Class[]{IA.class}, p);
        System.out.println(pl.paly());
        //利用构造函数实现绑定关系
        IAProxy pp = new IAProxy(IA.class,"abc");
        IA ph = (IA) Proxy.newProxyInstance(IA.class.getClassLoader(), new Class[]{IA.class}, pp);
        System.out.println(ph.paly());
    }

}