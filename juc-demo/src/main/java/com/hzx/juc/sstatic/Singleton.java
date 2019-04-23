package com.hzx.juc.sstatic;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:17 2019/4/18
 */
public class Singleton {

    private static Singleton singleton = null;


    static {
        System.out.println("static");
    }


    public static int value1 ;

    static {
        System.out.println("value1__:"+value1);
    }

    public static int value2 = 1;

    static {
        System.out.println("value2__:"+value2);
    }

    {
        System.out.println("非静态代码块1");
        value2++;
        System.out.println("非静态代码块1，value2:"+value2);
    }

    private Singleton(){
        System.out.println("value1:"+value1);
        value1++ ;
        System.out.println("value1_:"+value1);
        System.out.println("value2:"+value2);
        value2 = value2+2;
        System.out.println("value2_:"+value2);
        System.out.println("construct");
    }

    {
        System.out.println("非静态代码块2");
    }

    public static Singleton getInstance(){
        if(singleton == null) {
            singleton = new Singleton();
        }
        return singleton;
    }

}
