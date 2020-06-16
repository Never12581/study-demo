package com.hzx.nowcoder.hechangdui;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {


        Scanner in = new Scanner(System.in);
        while (in.hasNext()) {
            int num = in.nextInt();
            if (num <= 2) {
                System.out.println(0);
            }
            int[] members = new int[num];//存储每一个数据元素
            int[] left_queue = new int[num];//数据元素从左到右对应的最大递增子序列数
            int[] right_queue = new int[num];//数据元素从右到左对应的最大递增子序列数

            for (int i = 0; i < num; i++) {//初始化各个数组数据
                members[i] = in.nextInt();
                left_queue[i] = 1;
                right_queue[i] = 1;
            }

            /**
             * 1,2,3,4,5,6,4,4,4,7
             * 对比 1 和 7 ， 7 比 1 大，那么 加上7
             * 至少会比 仅计算 1 的最长递增子序列 多 1
             * 这就是 left_queue[i] = left_queue[j] + 1 的来源
             */
            for (int i = 0; i < num; i++) {
                for (int j = 0; j < i; j++) {
                    if (members[i] > members[j] && left_queue[j] + 1 > left_queue[i])
                        left_queue[i] = left_queue[j] + 1;
                }
            }

            for (int i = num - 1; i >= 0; i--) {
                for (int j = num - 1; j > i; j--) {
                    if (members[i] > members[j] && right_queue[j] + 1 > right_queue[i])
                        right_queue[i] = right_queue[j] + 1;
                }
            }
            int max = 0;
            for (int i = 0; i < num; i++) {
                if (left_queue[i] + right_queue[i] > max)
                    max = left_queue[i] + right_queue[i];
            }
            System.out.println(num - max + 1);
        }
    }


    // 186 186 186 150 200 160 130 197 200 200
    // 还是错了
    public static void _main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = Integer.parseInt(sc.nextLine());
        String param = sc.nextLine();
        String[] params = param.split(" ");
        if (params.length != num) {
            return;
        }
        int[] pparms = new int[num];
        for (int i = 0; i < num; i++) {
            pparms[i] = Integer.parseInt(params[i]);
        }

        // 暴力法
        // 记录最小值
        int min = 0;
        int isOverride = 0;

        for (int i = 0; i < num; i++) {

            int left = leftDeal(pparms, i);
            int right = rightDeal(pparms, i);
            int total = left + right;
            if (isOverride == 0) {
                isOverride = 1;
                min = total;
            } else {
                if (total < min) {
                    min = total;
                }
            }
        }
        System.out.println(min);
    }


    // 主要错在这个方法上

    /**
     * 虽然从1 开始算，但是不一定要 1 开始，
     * 也可以从 2 开始
     * 所以要计算最长子序列的长度
     */
    private static int leftDeal(int[] pparmas, int i) {
        int out = 0;
        int current = 0;
        int next = 1;
        while (next <= i) {
            if (pparmas[current] < pparmas[next]) {
                current = next;
                next++;
            } else {
                next++;
                out++;
            }
        }
        return out;
    }

    private static int rightDeal(int[] pparmas, int i) {
        int out = 0;
        int current = i;
        int next = current + 1;
        while (next < pparmas.length) {
            if (pparmas[current] > pparmas[next]) {
                current = next;
                next++;
            } else {
                next++;
                out++;
            }
        }
        return out;
    }

}
