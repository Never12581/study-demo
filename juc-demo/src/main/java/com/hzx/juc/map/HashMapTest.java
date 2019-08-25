package com.hzx.juc.map;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: bocai.huang
 * @Descripition: 以简单例子模拟hashmap在1.7版本进入死循环
 * @Date: Create in 13:56 2019/3/22
 */
public class HashMapTest {

    private static List<Integer> list = new ArrayList<>(8);

    static {
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        list.add(8);
    }

    public static void main(String[] args) {
        // String s = "adsfsasdfzxcvadf156as4d6f5aadf4s65df4a";
        String s = "a";
        int length = 8 ;
        System.out.println("hash:"+hash(s));
        System.out.println("map中的下标 "+calcuIndex(hash(s),length) ) ;
        System.out.println("hash & oldTabl " + (hash(s)& 8));
        System.out.println("=================================");
        length = 8 << 1 ;
        System.out.println("hash:"+hash(s));
        System.out.println("新map中的下标 "+calcuIndex(hash(s),length) );
        System.out.println("hash & oldTabl " + (hash(s)& 8));

    }

    static final int calcuIndex(int hash , int length) {
        return (length - 1) & hash;
    }

    static final int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }

}
