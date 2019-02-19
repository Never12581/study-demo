package com.hzx.sort;

import com.hzx.sort.heap.HeapSort;
import org.junit.Test;

/**
 * @Author: bocai.huang
 * @Descripition: 堆排序测试类
 * @Date: Create in 14:56 2019/2/18
 */
public class HeapSortTest {

    @Test
    public void testHeapArrange() {
        int[] array = new int[] { 3, 2, 4, 5, 3, 21, 53 };
        System.out.println("before heap arrange.");
        BaseSort.printArray(array);
        HeapSort.sort(array);
        System.out.println("after heap arrange.");
        BaseSort.printArray(array);
    }

}
