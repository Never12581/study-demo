package com.hzx.sort;

import java.util.ArrayList;
import java.util.List;
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
        int size = 50000000;
        int[] arr = new int[size];
        Random random = new Random();
        for(int i = 0 ; i < size ; i++){
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }
        return arr;
    }

    static int[] copyArray(int[] array) {
        int[] arr = new int[array.length];
        for(int i = 0 ; i < array.length ; i ++) {
            arr[i] = array[i];
        }
        return arr;
    }

    static Integer[] copyArray2ListInteger(int[] array) {
        Integer[] arr = new Integer[array.length];
        for(int i = 0 ; i < array.length ; i ++) {
            arr[i] = array[i];
        }
        return arr;
    }

    static Integer[] randomArrayInteger(){
        Integer[] arr = new Integer[1000000];
        Random random = new Random();
        for(int i = 0 ; i < 1000000 ; i++){
            arr[i] = random.nextInt(Integer.MAX_VALUE);
        }
        return arr;
    }

}
