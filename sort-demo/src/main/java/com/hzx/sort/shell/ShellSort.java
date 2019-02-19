package com.hzx.sort.shell;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 18:02 2019/2/15
 */
public interface ShellSort {

    int DEFAULT_PARAGRAPH = 3;

    static void sort(int[] array) {
        int length = array.length;
        int h = 1;
        while (h < length / DEFAULT_PARAGRAPH) {
            //h的取值序列为1, 4, 13, 40, ...
            h = DEFAULT_PARAGRAPH * h + 1;
        }
        while (h >= 1) {
            int n, i, j, k;
            //分割后，产生n个子序列
            for (n = 0; n < h; n++) {
                //分别对每个子序列进行插入排序
                for (i = n + h; i < length; i += h) {
                    for (j = i - h; j >= 0 && array[i] < array[j]; j -= h) {

                    }
                    int tmp = array[i];
                    for (k = i; k > j + h; k -= h) {
                        array[k] = array[k - h];
                    }
                    array[j + h] = tmp;
                }
            }
            h = h / DEFAULT_PARAGRAPH;
        }
    }

}
