package com.hzx.nowcoder;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/**
 * 字符个数统计
 */
public class CharacterCount {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < s.length(); i++) {
            int k = (int)s.charAt(i);
            if (k >=0 && k<= 127) {
                set.add(k);
            }
        }
        System.out.println(set.size());
    }
}
