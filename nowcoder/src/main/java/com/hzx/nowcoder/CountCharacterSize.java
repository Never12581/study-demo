package com.hzx.nowcoder;

import java.util.Scanner;

public class CountCharacterSize {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String source = scanner.nextLine();
        String a = scanner.nextLine();
        int count = 0 ;
        for (int i = 0 ; i < source.length() ; i++) {
            String b = String.valueOf(source.charAt(i));
            if (b.equalsIgnoreCase(a)) {
                count++;
            }
        }
        System.out.println(count);
    }
}
