package com.hzx.sort.bubble;

import com.hzx.sort.BaseSort;

/**
 * @Author: bocai.huang
 * @Descripition: bubble sort 冒泡排序 O(n2)
 * @Date: Create in 16:38 2019/2/15
 */
public interface BubbleSort {

    /**
     *  冒泡排序：
     *
     */
    static void sort(int[] array) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length - i - 1; j++) {
                if (array[j] > array[j + 1]) {
                    BaseSort.swap(array, j, j + 1);
                }
            }
        }
    }

}
