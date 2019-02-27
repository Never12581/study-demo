package com.hzx.sort.insertion;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 17:14 2019/2/15
 */
public interface InsertionSort {

    /**
     * 从第一张开始排，所以之前的一定都是有序的。插入下一个数据时（即开启下一次for循环时）
     * 从后往前（i之前）遍历所有数据，如果存在数据（j下标） 大于 i所在的数据 则将 j到i-1 之间数据往后移再将  i 的数据赋值给 j
     */
    static void sort(int[] array) {
        int length = array.length;
        int i, j;
        for (i = 1; i < length; i++) {

            for (j = i - 1; j >= 0 && array[i] < array[j]; j--) {
            }

            //这里跳出内层循环，a[i]应被插入到a[j]后
            int tmp = array[i];
            for (int k = i; k > j + 1; k--) {
                array[k] = array[k - 1];
            }
            array[j + 1] = tmp;

        }
    }

    /**
     * @param array
     * @param start  起始游标
     * @param length 以起始游标开始的数组长度
     */
    static void sort(int[] array, int start, int length) {
        if (start < 0) {
            throw new IllegalArgumentException("start value cannot less than 0 !");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length value cannot less than 0 !");
        }
        if ((start + length) > array.length) {
            throw new IllegalArgumentException("start plus length huger than array`s length !");
        }
        int i, j;
        for (i = start + 1; i < (start + length); i++) {
            for (j = i - 1; j >= start && array[i] < array[j]; j--) {
            }

            //这里跳出内层循环，a[i]应被插入到a[j]后
            int tmp = array[i];
            for (int k = i; k > j + 1; k--) {
                array[k] = array[k - 1];
            }
            array[j + 1] = tmp;
        }
    }

}
