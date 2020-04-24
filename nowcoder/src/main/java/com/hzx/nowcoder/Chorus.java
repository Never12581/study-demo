package com.hzx.nowcoder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * （
 * 合唱队
 * https://www.nowcoder.com/practice/6d9d69e3898f45169a441632b325c7b4?tpId=37&tqId=21247&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
 * 动态规划，下次做
 */
public class Chorus {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str;
        while ((str = br.readLine()) != null) {
            int total = Integer.parseInt(str);
            int[] data = new int[total];
            int[] reverseData = new int[total];
            // 处理身高数据
            str = br.readLine();
            String[] arr = str.split(" ");

            for (int i = 0; i < total; ++i) {
                data[i] = Integer.parseInt(arr[i]);
                reverseData[total - 1 - i] = data[i];
            }

            int[] asc = calculate(data);
            int[] desc = calculate(reverseData);
            int ret = 0;
            for (int i = 1; i <= total; i++) {
                ret = Math.max(asc[i] + desc[total - i + 1] - 1, ret);
            }

            System.out.println(total - ret);
        }
    }

    private static int[] calculate(int[] data) { // max[i]存储最长子序列长度为i的时候,最小的序列结尾.
        int total = data.length;
        int[] max = new int[total];
        int[] dp = new int[total + 1];
        int index = 0;
        max[index] = -1;
        for (int i = 0; i < total; i++) {
            if (data[i] > max[index]) {
                max[++index] = data[i];
                dp[i + 1] = dp[i] + 1;
            } else {
                for (int j = 0; j <= index; j++) { // 因为i的循环顺序是从小到大的,所以保证了当max[j]的数都是在i之前按序列顺序先后出现的.
                    if (data[i] <= max[j]) {
                        max[j] = data[i];
                        break;
                    }
                }
                dp[i + 1] = dp[i];
            }
        }

        return dp;
    }
}
