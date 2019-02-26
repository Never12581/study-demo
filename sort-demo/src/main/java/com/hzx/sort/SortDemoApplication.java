package com.hzx.sort;

import com.hzx.sort.heap.HeapSort;
import com.hzx.sort.insertion.InsertionSort;
import com.hzx.sort.merge.MergeSort;
import com.hzx.sort.quick.QuickSort;
import com.hzx.sort.selection.SelectionSort;
import com.hzx.sort.shell.ShellSort;

import java.util.Random;

public class SortDemoApplication {

    public static void main1(String[] args){
        int[] array = new int[]{9,23,12,543,23,32,43,13,42};
        InsertionSort.sort(array,3,2);
        // InsertionSort.sort(array);
        BaseSort.printArray(array);
    }

    public static void main(String[] args) {

        System.out.println("==============Heap=============");
        int[] array = randomArray();
        System.out.println("before sort : ");
        long start = System.currentTimeMillis();
        HeapSort.sort(array);
        System.out.println("Heap sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");

        System.out.println("==============Shell=============");
        array = randomArray();
        System.out.println("before sort : ");
        start = System.currentTimeMillis();
        ShellSort.sort(array);
        System.out.println("Shell sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");


        System.out.println("==============Quick=============");
        array = randomArray();
        System.out.println("before sort : ");
        start = System.currentTimeMillis();
        QuickSort.sort(array, 0, array.length - 1);
        System.out.println("Quick sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");

        System.out.println("==============Merge=============");
        array = randomArray();
        System.out.println("before sort : ");
        start = System.currentTimeMillis();
        MergeSort.sort(array, 0, array.length - 1);
        System.out.println("Merge sort cost time : " + (System.currentTimeMillis() - start));
        System.out.println("after sort : ");

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

    public static int[] randomArray(){
        int[] arr = new int[1000000];
        Random random = new Random();
        for(int i = 0 ; i < 1000000 ; i++){
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }
        return arr;
    }

}

