package com.hzx.nowcoder;

import java.util.Scanner;

public class StringSplit {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String s1 = scanner.nextLine();
        if (s1.length() > 100) {
            return;
        }
        String s2 = scanner.nextLine();
        if (s2.length() > 100) {
            return;
        }

        write(s1);
        write(s2);

    }

    private static void write(String source ) {
        for (int i = 0; i < source.length(); i = i + 8) {
            int index = i + 8;
            if (index > source.length()) {
                index = source.length();
                String s = source.substring(i,index);
                StringBuilder d = new StringBuilder(s);
                int k = 0 ;
                while (k < (8-s.length())) {
                    k++;
                    d.append(0);
                }
                System.out.println(d.toString());

            } else {
                System.out.println(source.substring(i, index));
            }

        }
    }

}
