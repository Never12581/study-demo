package com.hzx.juc.proxy.jdk;

import com.hzx.juc.proxy.user.User;
import com.hzx.juc.proxy.user.UserImpl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:58 2019/8/26
 */
public class JDKProxyTest {

    public static void main(String[] args) {
        UserImpl user = new UserImpl();
        User proxy = (User) Proxy.newProxyInstance(User.class.getClassLoader(), new Class[]{User.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                System.out.println("我变多了，也变强了！");
                return method.invoke(user,args);
            }
        });
        proxy.say();
    }

}
