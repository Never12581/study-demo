package com.hzx.nowcoder;

import java.util.Scanner;

/**
 * 汽水瓶子
 * https://www.nowcoder.com/practice/fe298c55694f4ed39e256170ff2c205f?tpId=37&tqId=21245&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
 */
public class WaterVaporBottle {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        for (;sc.hasNextInt();) {
            int k = sc.nextInt();
            int count = 0;
            int a = 0 ;
            while ( (a =(k / 3)) > 0) {
                count = count + a;
                k = a + k % 3;
                if (k==2){
                    count++;
                    break;
                }
            }
            System.out.println(count);
        }
    }


}
