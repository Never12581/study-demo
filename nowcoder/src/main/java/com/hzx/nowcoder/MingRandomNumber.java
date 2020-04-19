package com.hzx.nowcoder;

import java.util.Scanner;
import java.util.TreeSet;

public class MingRandomNumber {
    /**
     * 先输入随机数数量
     * 后输入随机数
     * 对随机数进行去重和排序
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            int num = scanner.nextInt();
            TreeSet<Integer> treeSet = new TreeSet<>();

            for (int i = 0; i < num; i++) {
                treeSet.add(scanner.nextInt());
            }

            // 输出
            treeSet.stream().forEach(e -> {
                System.out.println(e);
            });
        }
    }
}
