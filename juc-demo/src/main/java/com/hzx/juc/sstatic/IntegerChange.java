package com.hzx.juc.sstatic;

import java.lang.reflect.Field;

public class IntegerChange {

    public static void main(String[] args) throws Exception {
        Integer a = Integer.parseInt("10");
        Integer b = Integer.valueOf(10);
        Integer c = 10;
        System.out.println(a.hashCode()==b.hashCode());
        System.out.println(a.hashCode()==c.hashCode());
        System.out.println(b.hashCode()==c.hashCode());
        changeValue(a, 100);
        System.out.printf("%d %d %d \n", a, b, c);
        changeValue(b, 200);
        System.out.printf("%d %d %d \n", a, b, c);
        changeValue(c, 300);
        System.out.printf("%d %d %d \n", a, b, c);
    }

    private static void changeValue(Integer i, int value) throws Exception {
        Field field = Integer.class.getDeclaredField("value");
        field.setAccessible(true);
        field.set(i, value);
    }
}