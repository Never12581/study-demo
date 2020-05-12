package com.hzx.juc.proxy.dynamic;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class IAProxy implements InvocationHandler {

    private String methodValue ;
    private String methodName ;

    IAProxy(String methodValue , String methodName) {
        this.methodValue = methodValue;
        this.methodName = methodName;
    }

    //代理执行方法
    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if (method.getName().equals(methodName)) {
            return methodValue;
        }
        return null;
    }

}