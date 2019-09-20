package com.hzx.stream;

import java.util.OptionalInt;
import java.util.stream.IntStream;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:53 2019/9/18
 */
public class IntStreamTest {

    public static void main(String[] args) {
        splitLine();
        IntStream intStream = IntStream.of(new int[]{1, 2, 3});
        intStream.forEach(System.out::println);
        splitLine();
        IntStream.of(new int[]{1, 2, 3}).forEach(System.out::println);
        splitLine();
        IntStream.range(1, 3).forEach(System.out::println);
        splitLine();
        OptionalInt optionalInt = IntStream.of(new int[]{1, 2, 3}).findFirst();
        System.out.println(optionalInt.getAsInt());
    }

    private static void splitLine(){
        System.out.println("==========================");
    }

}
