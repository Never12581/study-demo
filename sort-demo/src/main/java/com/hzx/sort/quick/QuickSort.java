package com.hzx.sort.quick;

/**
 * @Author: bocai.huang
 * @Descripition: 快速排序 思想：快速排序的主要思想如下：假设待排序数组为a[0..N-1]，递归的对该数组执行以下过程：选取一个切分元素，而后通过数组元素的交换将这个切分元素移动到位置j，使得所有a[0..j-1]的元素都小于等于a[j]，所有a[j+1..N-1]的元素都大于等于a[j]。
 * @Date: Create in 09:52 2019/2/18
 */
public interface QuickSort {

    /**
     * @param s 排序数组
     * @param l 左元素
     * @param r 右元素
     */
    static void sort(int s[], int l, int r) {
        if (l < r) {
            int i = l;
            int j = r;
            int x = s[l];
            while (i < j) {
                // 从右向左找第一个小于x的数
                while (i < j && s[j] >= x) {
                    j--;
                }
                if (i < j) {
                    s[i++] = s[j];
                }
                // 从左向右找第一个大于等于x的数
                while (i < j && s[i] < x) {
                    i++;
                }
                if (i < j) {
                    s[j--] = s[i];
                }
            }
            s[i] = x;
            // 递归调用
            sort(s, l, i - 1);
            sort(s, i + 1, r);
        }
    }

    static void sort_mine(int[] array, int l, int r) {
        if (l < r) {
            // 作为基准值
            int x = array[l];
            int i = l;
            int j = r;
            while (i < j) {
                // 先从后往前，查找一个值小于基准值
                while (i < j && array[j] >= x) {
                    j--;
                }
                if (i < j) {
                    array[i++] = array[j];
                }

                // 再从前往后找，查找到一个大于基准值的
                while (i < j && array[i] < x) {
                    i++;
                }
                if (i < j) {
                    array[j--] = array[i];
                }
                array[i] = x;
            }
            // 递归调用
            sort_mine(array, l, i - 1);
            sort_mine(array, i + 1, r);
        }

    }

}
