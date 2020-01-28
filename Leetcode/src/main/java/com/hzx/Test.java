package com.hzx;

import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;

/**
 * @author: bocai.huang
 * @create: 2020-01-11 16:38
 **/
public class Test {

    private static List<Runnable> firstList = new LinkedList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 128 ; i++) {
            final int k = i;
            Thread temp = new Thread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("第" + k + "次添加线程" + "添加了当前线程" + Thread.currentThread().getName());
                }
            });
            temp.start();
            firstList.add(temp);
        }

        System.out.println(firstList.size());

        ByteBuffer.allocate(1024*1000*200);

    }

}
