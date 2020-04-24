package com.hzx.nowcoder;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * 购物单
 * https://www.nowcoder.com/practice/f9c6f980eeec43ef85be20755ddbeaf4?tpId=37&tqId=21239&tPage=1&rp=&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
 */
public class ShoppingList {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String str = br.readLine();
        int N = Integer.parseInt(str.split(" ")[0]) / 10;
        int m = Integer.parseInt(str.split(" ")[1]);
        int[] v = new int[m + 1];
        int[] p = new int[m + 1];
        int[] q = new int[m + 1];
        boolean[] flags = new boolean[m + 1];
        int[][] res = new int[m + 1][N + 1];
        for (int i = 1; i <= m; i++) {
            String[] strings = br.readLine().split(" ");
            v[i] = (Integer.parseInt(strings[0])) / 10;
            p[i] = Integer.parseInt(strings[1]) * v[i];
            q[i] = Integer.parseInt(strings[2]);
        }

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= N; j++) {
                if (q[i] == 0) {
                    if (v[i] <= j) {
                        res[i][j] = Math.max(res[i - 1][j], res[i - 1][j - v[i]] + p[i]);
                    }
                } else {
                    if (v[i] + v[q[i]] <= j) {
                        res[i][j] = Math.max(res[i - 1][j], res[i - 1][j - v[i]] + p[i]);
                    }
                }
            }
        }
        System.out.println(res[m][N] * 10);

    }

}
