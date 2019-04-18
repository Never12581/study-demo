package com.hzx.sort;

import java.util.Random;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 16:51 2019/2/15
 */
public interface BaseSort {

    // void sort(int[] array);

    static void swap(int[] array, int index1, int index2) {
        if (index1 != index2) {
            array[index1] = array[index1] + array[index2];
            array[index2] = array[index1] - array[index2];
            array[index1] = array[index1] - array[index2];
        }
    }

    static void printArray(int[] array) {
        int length = array.length;
        for (int i = 0; i < length; i++) {
            System.out.print(array[i] + ",");
        }
        System.out.println();
    }

    static int[] randomArray(){
        int[] arr = new int[100000];
        Random random = new Random();
        for(int i = 0 ; i < 100000 ; i++){
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }
        return arr;
    }

}
