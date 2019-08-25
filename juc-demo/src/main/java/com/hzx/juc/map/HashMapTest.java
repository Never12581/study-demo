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
        // new Thread(() -> {
        //
        //     for (int i = 9; i < 81; i++) {
        //         List<Integer> tempList = list;
        //         tempList.add(i);
        //         list = tempList;
        //     }
        // }).start();
        //
        // new Thread(() -> {
        //     for (Integer i : list) {
        //         System.out.println(i);
        //     }
        // }).start();

        System.out.println(1&0);

    }

}
