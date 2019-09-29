package com.hzx.sort.heap;

import static com.hzx.sort.BaseSort.swap;

/**
 * @Author: bocai.huang
 * @Descripition: 堆排序
 * @Date: Create in 14:42 2019/2/18
 */
public interface HeapSort {

    /**
     * 首先是完全二叉树 （从第一层开始，除了最后一层，第N曾第个数都是2的N次方；最后一层的个数，都必须紧贴左边；具有此两个特征的二叉树被成为完全二叉树）
     * ----------------------------
     * 根堆：在完全二叉树的基础上，对于一个拥有父节点的子节点，其数值均不小于父节点的值，层层递推可得出根节点的值最小，称为小根堆
     * 对于一个拥有父节点的子节点，其数值均大于父节点的值，层层递推可得出根节点的值最大，称为大根堆
     * ----------------------------
     * 堆排序的思路：
     * 1、先将数组元素交换使满足 大根堆或者小根堆 的描述
     * 2、将根节点与最后一个节点置换（这样的话，最大值或者最小值就在数组的最后一位了）
     * 3、将除了最后一个节点以外的 完全二叉树（此时已经不满足根堆的要求了） 整理成根堆，再重复2和3
     */

    static void sort(int[] array) {
        for (int i = array.length / 2 - 1; i >= 0; i--) {
            adjustHeap(array, i, array.length);
        }

        for (int j = array.length - 1; j > 0; j--) {
            swap(array, 0, j);
            adjustHeap(array, 0, j);
        }

    }

    /**
     * 这tm的是核心中的核心！
     *
     * @param array
     * @param i
     * @param length
     */
    static void adjustHeap(int[] array, int i, int length) {
        int temp = array[i];
        for (int k = 2 * i + 1; k < length; k = 2 * k + 1) {
            if (k + 1 < length && array[k] < array[k + 1]) {
                k++;
            }

            if (array[k] > temp) {
                swap(array, i, k);
                i = k;
            } else {
                break;
            }
        }
    }

}
