package com.hzx.juc.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class IAProxy implements InvocationHandler {

    private Class inter_face;
    private String methodValue ;

    IAProxy(Class inter_face , String methodValue) {
        this.inter_face = inter_face;
        this.methodValue = methodValue;
    }

    //空构造函数
    IAProxy() {

    }

    //代理执行方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        return methodValue;
    }

    //一种代理方式

    public static Object bind(Class inter_face) {
        IAProxy p = new IAProxy();
        return Proxy.newProxyInstance(inter_face.getClassLoader(), new Class[]{inter_face}, p);
    }

}