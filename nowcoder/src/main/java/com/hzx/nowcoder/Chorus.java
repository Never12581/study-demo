package com.hzx.nowcoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * （
 * 合唱队
 * https://www.nowcoder.com/practice/6d9d69e3898f45169a441632b325c7b4?tpId=37&tqId=21247&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
 * 动态规划，下次做
 */
public class Chorus {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            int num = Integer.parseInt(sc.nextLine());
            String s = sc.nextLine();
            String[] ss = s.split(" ");
            List<Integer> list = new ArrayList<>(ss.length);
            for (String sss : ss) {
                list.add(Integer.parseInt(sss));
            }

            //
            int mid = 0 ;
            for (int i = 0; i < list.size(); i++) {
                // 获取当前的数 默认其为最高位
                mid = list.get(i);
                for (int j = i ; j >=0 ; j-- ) {

                }
            }
        }
    }
}
