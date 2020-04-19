package com.hzx.nowcoder;

import java.util.Scanner;

public class BinaryConversion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNext()){
            System.out.println(Integer.parseInt(sc.next().replaceAll("x",""),16));
        }
    }
}
