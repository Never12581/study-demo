package com.hzx.sort;

import com.hzx.sort.heap.HeapSort;
import com.hzx.sort.heap.MaxHeap;
import java.util.ArrayList;
import java.util.List;
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

    @Test
    public void testMaxHeap(){
        List<Integer> list = new ArrayList<>();
        list.add(27);
        list.add(33);
        list.add(15);
        list.add(19);
        list.add(7);
        list.add(90);
        int size = list.size();
        MaxHeap<Integer> maxHeap = new MaxHeap<>(list);
        for(int i = 0 ; i < size  ; i++) {
            System.out.println(maxHeap.popMax());
        }

    }

}
