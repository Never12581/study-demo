package com.hzx.sort;

import com.hzx.sort.heap.ArrayMaxHeap2;
import com.hzx.sort.heap.HeapSort;
import com.hzx.sort.heap.IntArrayMaxHeap;
import com.hzx.sort.heap.IntArrayMaxHeap2;
import com.hzx.sort.radix.RadixSort;
import java.util.List;
import org.junit.Test;

import static com.hzx.sort.BaseSort.copyArray;
import static com.hzx.sort.BaseSort.copyArray2ListInteger;
import static com.hzx.sort.BaseSort.randomArray;

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
        int[] arrayK = randomArray();

        System.out.println("==============Heap6 int参数类型，引入了实际大小参数 =============");
        int[] array = copyArray(arrayK);
        System.out.println("before sort : ");
        long start = System.currentTimeMillis();
        new IntArrayMaxHeap2(array).sort();
        System.out.println("Heap sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");
        boolean flag = true;
        for(int i = 1 ; i < array.length ; i++){
            if(array[i-1] > array[i]) {
                flag = false;
                break;
            }
        }
        System.out.println("排序："+flag);

        System.out.println("==============Heap 网上抄的 =============");
        array = copyArray(arrayK);
        System.out.println("before sort : ");
        start = System.currentTimeMillis();
        HeapSort.sort(array);
        System.out.println("Heap sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");

        flag = true;
        for(int i = 1 ; i < array.length ; i++){
            if(array[i-1] > array[i]) {
                flag = false;
                break;
            }
        }
        System.out.println("排序："+flag);

        System.out.println("==============Heap4 int 参数类型，未引入实际大小参数 =============");
        array = copyArray(arrayK);
        System.out.println("before sort : ");
        start = System.currentTimeMillis();
        new IntArrayMaxHeap(array).sort();
        System.out.println("Heap sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");
        flag = true;
        for(int i = 1 ; i < array.length ; i++){
            if(array[i-1] > array[i]) {
                flag = false;
                break;
            }
        }
        System.out.println("排序："+flag);



//        System.out.println("==============Shell=============");
//        array = randomArray();
//        System.out.println("before sort : ");
//        start = System.currentTimeMillis();
//        ShellSort.sort(array);
//        System.out.println("Shell sort cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sort : ");
//
//
//        System.out.println("==============Quick=============");
//        array = randomArray();
//        System.out.println("before sort : ");
//        start = System.currentTimeMillis();
//        QuickSort.sort(array, 0, array.length - 1);
//        System.out.println("Quick sort cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sort : ");
//
//        System.out.println("==============Merge=============");
//        array = randomArray();
//        System.out.println("before sort : ");
//        start = System.currentTimeMillis();
//        MergeSort.sort(array, 0, array.length - 1);
//        System.out.println("Merge sort cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sort : ");
//
//        System.out.println("==============Bucket=============");
//        array = randomArray();
//        System.out.println("before sort : ");
//        start = System.currentTimeMillis();
//        BucketSort.sort(array);
//        System.out.println("Bucket sort cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sort : ");
//
//        System.out.println("==============Radix=============");
//        array = randomArray();
//        System.out.println("before sort : ");
//        start = System.currentTimeMillis();
//        RadixSort.lsdSort(array);
//        System.out.println("Radix sort cost time : " + (System.currentTimeMillis() - start));
//        System.out.println("after sort : ");

        // System.out.println("==============Insertion=============");
        // array = randomArray();
        // System.out.println("before sort : ");
        // start = System.currentTimeMillis();
        // InsertionSort.sort(array);
        // System.out.println("Insertion sort cost time : " + (System.currentTimeMillis() - start));
        // System.out.println("after sort : ");
        //
        // System.out.println("==============Selection=============");
        // array = randomArray();
        // System.out.println("before sort : ");
        // start = System.currentTimeMillis();
        // SelectionSort.sort(array);
        // System.out.println("Selection sort cost time : " + (System.currentTimeMillis() - start));
        // System.out.println("after sort : ");
        //
        // System.out.println("===============Bubble================");
        // array = randomArray();
        // System.out.println("before sort : ");
        // start = System.currentTimeMillis();
        // BubbleSort.sort(array);
        // System.out.println("Bubble sort cost time : " + (System.currentTimeMillis() - start));
        // System.out.println("after sort : ");

    }



}
