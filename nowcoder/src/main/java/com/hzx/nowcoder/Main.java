package com.hzx.nowcoder;

import java.util.Scanner;

/**
 * 一个数字转二进制后，1的数量
 */
public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int m = sc.nextInt();
        String s = Integer.toBinaryString(m);
        int count = 0 ;
        for (int i = 0 ; i< s.length() ; i++) {
            int k = (int)s.charAt(i);
            if (k== 49) {
                count++;
            }
        }
        System.out.println(count);
    }
}