package com.hzx.juc.classloader;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:17 2019/12/19
 */
public class TestClassLoader {

    public static void main(String[] args) throws ClassNotFoundException {
        String classPath = "/Users/huangbocai/TestUser.class";
        MyClassLoader myClassLoader = new MyClassLoader(classPath);
        String className = "com.hzx.juc.classloader.TestUser";
        Class clazz = myClassLoader.loadClass(className);

        System.out.println(clazz == null);
        
    }

}
