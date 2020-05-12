package com.hzx.juc.proxy.dynamic;

import java.lang.reflect.Proxy;

public class Test {

    public static void main(String[] args) {

        IAProxy pp = new IAProxy("abc","getName");
        IA ph = (IA) Proxy.newProxyInstance(IA.class.getClassLoader(), new Class[]{IA.class}, pp);
        System.out.println(ph.getName());


        pp = new IAProxy("def","getName");
        ph = (IA) Proxy.newProxyInstance(IA.class.getClassLoader(), new Class[]{IA.class}, pp);
        System.out.println(ph.getName());
    }

}