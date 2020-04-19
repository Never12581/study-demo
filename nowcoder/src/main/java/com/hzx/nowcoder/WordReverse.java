package com.hzx.nowcoder;

import java.util.Scanner;

public class WordReverse {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        System.out.println(reverse(s));
    }

    /**
     * 反转句子
     *
     * @param sentence 原句子
     * @return 反转后的句子
     */
    public static String reverse(String sentence) {
        String[] words = sentence.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = words.length; i > 0; i--) {
            sb.append(words[i - 1]);
            if (i != 0) {
                sb.append(" ");
            }
        }
        return sb.toString();
    }

}
