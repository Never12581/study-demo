package com.hzx.nowcoder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * （
 * 合唱队
 * https://www.nowcoder.com/practice/6d9d69e3898f45169a441632b325c7b4?tpId=37&tqId=21247&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
 * 动态规划，下次做
 * <p>
 * 计算最少出列多少位同学，使得剩下的同学排成合唱队形
 * <p>
 * 说明：
 * <p>
 * N位同学站成一排，音乐老师要请其中的(N-K)位同学出列，使得剩下的K位同学排成合唱队形。
 * 合唱队形是指这样的一种队形：设K位同学从左到右依次编号为1，2…，K，他们的身高分别为T1，T2，…，TK，   则他们的身高满足存在i（1<=i<=K）使得T1<T2<......<Ti-1<Ti>Ti+1>......>TK。
 * <p>
 * 你的任务是，已知所有N位同学的身高，计算最少需要几位同学出列，可以使得剩下的同学排成合唱队形。
 * <p>
 * 输入描述:
 * 整数N
 * <p>
 * 输出描述:
 * 最少需要几位同学出列
 * <p>
 * 示例1
 * 输入
 * 复制
 * 8
 * 186 186 150 200 160 130 197 200
 * 输出
 * 复制
 * 4
 * 动态规划思想：
 *
 * 最长递增子序列，两个最长递增子序列 相加 - 1
 */
public class Chorus {

    public static void main(String[] args) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String str;
        while ((str = br.readLine()) != "") {
            if ("".equals(str) || str == null) {
                break;
            }
            int total = Integer.parseInt(str);
            int[] data = new int[total];
            str = br.readLine();
            // 处理身高数据
            String[] arr = str.split(" ");
            for (int i = 0; i < total; ++i) {
                data[i] = Integer.parseInt(arr[i]);
            }
            int result = calculateError(data);
            System.out.println(result);
        }
    }

    /**
     * 算法不正确：
     * 186 186 150 200 160 130 197 200
     * 0    1   2   2   3   4   4   4
     * 4    3   3   2   2   2   1   0
     */
    private static int calculateError(int[] data) { // max[i]存储最长子序列长度为i的时候,最小的序列结尾.
        int length = data.length;
        int[] left = new int[length];
        int[] right = new int[length];
        for (int i = 0; i < length; i++) {
            if (i == 0) {
                left[i] = 0;
                continue;
            }
            if (data[i] <= data[i - 1]) {
                left[i] = left[i - 1] + 1;
            } else {
                left[i] = left[i - 1];
            }
        }
        for (int i = length - 1; i >= 0; i--) {
            if (i == length - 1) {
                right[i] = 0;
                continue;
            }
            if (data[i] <= data[i + 1]) {
                right[i] = right[i + 1] + 1;
            } else {
                right[i] = right[i + 1];
            }
        }

        int min = length;
        for (int i = 0; i < length; i++) {
            int temp = left[i] + right[i];
            if (temp < min) {
                min = temp;
            }
        }

        return min;
    }
}
