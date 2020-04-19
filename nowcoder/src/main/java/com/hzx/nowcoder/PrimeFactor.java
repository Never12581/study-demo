package com.hzx.nowcoder;

import java.util.Scanner;

public class PrimeFactor {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long ulDataInput = scanner.nextLong();
        String s = getResult(ulDataInput);
        System.out.println(s);
    }

    public static String getResult(long ulDataInput) {
        StringBuilder sb = new StringBuilder();
        for (int i = 2; i <= ulDataInput; i++) {
            if (ulDataInput % i == 0) {
                sb.append(i).append(" ");
                ulDataInput = ulDataInput / i;
                i = 1 ;
            }
        }
        return sb.toString();
    }

}
