package com.hzx.nowcoder;

import java.text.DecimalFormat;
import java.util.Scanner;

/**
 * 求解立方根
 */
public class CubeRoot {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String str = sc.nextLine();
        System.out.println(getCubeRoot(str));
    }

    public static String getCubeRoot(String str) {
        double num = Double.parseDouble(str);
        double dis = 1.0;
        double start = 0.1;
        for (double i = 0.1; dis > 0.0; i += 0.01) {
            double temp = i * i * i;
            dis = num - temp;
            start = i;
        }
        DecimalFormat df = new DecimalFormat("#.0");
        String ss = df.format(start);
        return ss;
    }
}
