package com.hzx.stream;

import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.function.IntPredicate;
import java.util.function.ToLongFunction;
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
        IntStream.of(new int[]{1, 2, 3}).filter(e -> e==1).forEach(System.out::println);
        splitLine();
        IntStream.range(1, 3).forEach(System.out::println);
        splitLine();
        OptionalInt optionalInt = IntStream.of(new int[]{1, 2, 3}).findFirst();
        System.out.println(optionalInt.getAsInt());

        splitLine();

        String s = "123";
        long l = toLong(s,k ->  Long.parseLong(k));
        System.out.println("s:"+s +" l :"+l);

        long c = toLong(1,k-> (long)k);
        System.out.println("传入: 1   c:"+c);
    }

    private static void splitLine(){
        System.out.println("==========================");
    }

    private static <T> long toLong (T str , ToLongFunction<T> toLongFunction) {
        return toLongFunction.applyAsLong(str);
    }

}
