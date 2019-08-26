package com.hzx.juc.proxy.cglib;

import com.hzx.juc.proxy.user.User;
import com.hzx.juc.proxy.user.UserImpl;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:27 2019/8/26
 */
public class CglibProxyTest {

    public static void main(String[] args) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(UserImpl.class);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy)
                    throws Throwable {
                System.out.println("我被代理了!");
                return methodProxy.invokeSuper(o,objects);
            }
        });
        User proxy = (User) enhancer.create();
        System.out.println(proxy.getClass());
        proxy.say();
    }

}
