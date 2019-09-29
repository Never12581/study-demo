package com.hzx.sort.selection;

import com.hzx.sort.BaseSort;

/**
 * @Author: bocai.huang
 * @Descripition: selection sort 选择排序 O(n2)
 * @Date: Create in 16:56 2019/2/15
 */
public interface SelectionSort {

    /**
     * 优于冒泡排序的原因是减少了 内存交换的次数
     */
    static void sort(int[] array) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            int min = i;
            for (int j = i + 1; j < length; j++) {
                if (array[j] < array[min]) {
                    min = j;
                }
            }
            if (min != i) {
                BaseSort.swap(array, i, min);
            }
        }
    }

}
