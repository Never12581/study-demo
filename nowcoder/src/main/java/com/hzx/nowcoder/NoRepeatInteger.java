package com.hzx.nowcoder;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class NoRepeatInteger {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int num = scanner.nextInt();
        String s = String.valueOf(num);
        StringBuilder sb = new StringBuilder();
        Set<String> set = new HashSet<>();
        for (int i = s.length() - 1; i >= 0; i--) {
            String k = String.valueOf(s.charAt(i));
            if (!set.contains(k)){
                sb.append(k);
                set.add(k);
            }
        }
        System.out.println(sb.toString());
    }

}
