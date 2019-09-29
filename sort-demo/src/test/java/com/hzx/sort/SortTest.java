package com.hzx.sort;

import com.hzx.sort.heap.ArrayMaxHeap;
import com.hzx.sort.heap.HeapSort;
import com.hzx.sort.heap.MaxHeap;
import com.hzx.sort.radix.RadixSort;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;

import static com.hzx.sort.BaseSort.randomArray;
import static com.hzx.sort.BaseSort.randomArrayInteger;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 11:01 2019/2/27
 */
public class SortTest {

    @Test
    public void test1(){
        int[] array = new int[]{9,23,12,543,23,32,43,13,42};
        RadixSort.lsdSort(array);
        BaseSort.printArray(array);
    }

    @Test
    public void test() {

        System.out.println("==============Heap=============");
        int[] array = randomArray();
        System.out.println("before sortRoot : ");
        long start = System.currentTimeMillis();
        HeapSort.sort(array);
        System.out.println("Heap sortRoot cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sortRoot : ");

        boolean flag = true;
        for(int i = 1 ; i < array.length ; i++){
            if(array[i-1] > array[i]) {
                flag = false;
                break;
            }
        }
        System.out.println("排序："+flag);


        System.out.println("==============Heap2=============");
        array = randomArray();
        List<Integer> list = new ArrayList<>(array.length);
        Arrays.stream(array).forEach(e->{
            list.add(e);
        });
        System.out.println("before sortRoot : ");
        start = System.currentTimeMillis();
        List<Integer> list2 = new MaxHeap<>(list).sort();
        System.out.println("Heap sortRoot cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sortRoot : ");
        flag = true;
        for(int i = 1 ; i < array.length ; i++){
            if(array[i-1] > array[i]) {
                flag = false;
                break;
            }
        }
        System.out.println("排序："+flag);

        System.out.println("==============Heap3=============");
        Integer[] array1 = randomArrayInteger();
        System.out.println("before sortRoot : ");
        start = System.currentTimeMillis();
        new ArrayMaxHeap<>(array1).sortRoot();
        System.out.println("Heap sortRoot cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sortRoot : ");
        flag = true;
        for(int i = 1 ; i < array1.length ; i++){
            if(array1[i-1] > array1[i]) {
                flag = false;
                break;
            }
        }
        System.out.println("排序："+flag);

//        System.out.println("==============Shell=============");
//        array = randomArray();
//        System.out.println("before sortRoot : ");
//        start = System.currentTimeMillis();
//        ShellSort.sortRoot(array);
//        System.out.println("Shell sortRoot cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sortRoot : ");
//
//
//        System.out.println("==============Quick=============");
//        array = randomArray();
//        System.out.println("before sortRoot : ");
//        start = System.currentTimeMillis();
//        QuickSort.sortRoot(array, 0, array.length - 1);
//        System.out.println("Quick sortRoot cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sortRoot : ");
//
//        System.out.println("==============Merge=============");
//        array = randomArray();
//        System.out.println("before sortRoot : ");
//        start = System.currentTimeMillis();
//        MergeSort.sortRoot(array, 0, array.length - 1);
//        System.out.println("Merge sortRoot cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sortRoot : ");
//
//        System.out.println("==============Bucket=============");
//        array = randomArray();
//        System.out.println("before sortRoot : ");
//        start = System.currentTimeMillis();
//        BucketSort.sortRoot(array);
//        System.out.println("Bucket sortRoot cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sortRoot : ");
//
//        System.out.println("==============Radix=============");
//        array = randomArray();
//        System.out.println("before sortRoot : ");
//        start = System.currentTimeMillis();
//        RadixSort.lsdSort(array);
//        System.out.println("Radix sortRoot cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sortRoot : ");

        // System.out.println("==============Insertion=============");
        // array = randomArray();
        // System.out.println("before sortRoot : ");
        // start = System.currentTimeMillis();
        // InsertionSort.sortRoot(array);
        // System.out.println("Insertion sortRoot cost time : " + (System.currentTimeMillis() - start));
        // System.out.println("after sortRoot : ");
        //
        // System.out.println("==============Selection=============");
        // array = randomArray();
        // System.out.println("before sortRoot : ");
        // start = System.currentTimeMillis();
        // SelectionSort.sortRoot(array);
        // System.out.println("Selection sortRoot cost time : " + (System.currentTimeMillis() - start));
        // System.out.println("after sortRoot : ");
        //
        // System.out.println("===============Bubble================");
        // array = randomArray();
        // System.out.println("before sortRoot : ");
        // start = System.currentTimeMillis();
        // BubbleSort.sortRoot(array);
        // System.out.println("Bubble sortRoot cost time : " + (System.currentTimeMillis() - start));
        // System.out.println("after sortRoot : ");

    }



}
