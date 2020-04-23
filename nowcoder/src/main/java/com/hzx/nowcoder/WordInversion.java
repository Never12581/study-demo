package com.hzx.nowcoder;

import java.util.Scanner;

/**
 * 单词倒排
 * https://www.nowcoder.com/practice/81544a4989df4109b33c2d65037c5836?tpId=37&tqId=21254&rp=0&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 * 注意点：
 */
public class WordInversion {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            s = s.replaceAll("[^a-zA-Z]+", " ");
            String[] ss = s.split(" ");
            int length = ss.length;
            for (int i = length - 1 ; i >=0; i--) {
                System.out.print(ss[i]);
                if (i != 0) {
                    System.out.print(" ");
                } else {
                    System.out.println();
                }
            }
        }
    }
}
