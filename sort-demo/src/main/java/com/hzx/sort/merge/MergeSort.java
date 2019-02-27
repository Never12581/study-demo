package com.hzx.sort.merge;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 11:03 2019/2/26
 */
public interface MergeSort {

    /**
     * 归并排序，将两个有序数组合并。以下算法是，直到数组中数量为1 ，自然是有序的，然后再进行归并排序，是一种递归的思想
     */

    static int[] sort(int[] array, int left, int right) {

        if (right == left) {
            return new int[] { array[left] };
        }

        int mid = right / 2 + left / 2;
        int[] l = sort(array, left, mid);
        int[] r = sort(array, mid + 1, right);
        return merge(l, r);
    }

    // 将两个数组合并成一个
    static int[] merge(int[] l, int[] r) {
        int[] result = new int[l.length + r.length];
        int p = 0;
        int lp = 0;
        int rp = 0;
        while (lp < l.length && rp < r.length) {
            result[p++] = l[lp] < r[rp] ? l[lp++] : r[rp++];
        }
        while (lp < l.length) {
            result[p++] = l[lp++];
        }
        while (rp < r.length) {
            result[p++] = r[rp++];
        }
        return result;
    }

}
