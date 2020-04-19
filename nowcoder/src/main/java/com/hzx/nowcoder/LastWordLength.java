package com.hzx.nowcoder;

import java.util.Scanner;

public class LastWordLength {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        String sourceStr = input.nextLine();

        int length = lastWordLength(sourceStr);

        System.out.println(length);
    }

    public static int lastWordLength(String sourceStr){
        String[] sStr = sourceStr.split(" ");
        return sStr[sStr.length-1].length();
    }

}
