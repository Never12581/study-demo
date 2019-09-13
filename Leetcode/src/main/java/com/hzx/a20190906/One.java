package com.hzx.a20190906;

import java.util.*;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:32 2019/9/6
 */
public class One {

    /**
     * 1. 合并时间区间(建议时间复杂度 O(n) )
     * 给定⼀一个按开始时间从⼩小到⼤大排序的时间区间集合，请将重叠的区间合并。时 间区间集合⽤用⼀一个⼆二维数组表示，
     * ⼆二维数组的每⼀一⾏行行表示⼀一个时间区间(闭区 间)，其中 0 位置元素表示时间区间开始，1 位置元素表示时间区间结束。
     * 例例 1:输⼊入:[ [1, 3], [2, 6], [8, 10], [15, 18] ]
     * 返回: [ [1, 6], [8, 10], [15, 18]]
     * 解释:时间区间 [1, 3] 和 [2, 6] 有部分重叠，合并之后为 [1, 6]
     * 例例 2:输⼊入:[[1, 4], [4, 5]] 返回:[[1, 5]]
     * 解释:时间区间[1，4] 和 [4，5]重叠了了⼀一个时间点，合并之后为 [1，5] 需要实现的⽅方法原型:int[][] merge(int[][] 1intervals)
     */

    private static int[][] merge(int[][] intervals) {
        if (intervals == null) {
            return null;
        }
        Integer index = 0;
        Map<Integer,int[]> map = new TreeMap<>();
        for (int[] i : intervals) {
            // 这些东西可以去掉，但是习惯了了
            if (i.length != 2) {
                continue;
            }
            int[] k = null;
            k = map.get(index);
            if (k == null) {
                k = i;
                map.put(index,k);
                continue;
            }
            // 先判断 i[0] 是否 大于 k[1]
            if (k[1] >= i[0]) {
                k[1] = i[1];
                continue;
            }
            // 到下一个桶
            index++;
            k = i;
            map.put(index,k);
        }

        // 这个轮询，没办法
        int[][] result = new int[map.size()][];
        for(int i = 0 ; i < index ; i++){
            result[i] = map.get(i);
        }

        return result;
    }

    public static void main(String[] args) {
        int[][] input = new int[4][];
        int[] i0 = new int[2];
        i0[0] = 1;
        i0[1] = 3;
        int[] i1 = new int[2];
        i1[0] = 2;
        i1[1] = 6;
        int[] i2 = new int[2];
        i2[0] = 8;
        i2[1] = 10;
        int[] i3 = new int[2];
        i3[0] = 15;
        i3[1] = 18;
        input[0] = i0;
        input[1] = i1;
        input[2] = i2;
        input[3] = i3;

        int[][] output = merge(input);
        for (int[] k : output) {
            System.out.println(k);
        }

    }

}
