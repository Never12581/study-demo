package com.hzx.sort.heap;

public interface MaxHeap<T extends Comparable<T>> {

    T findMax();

    T popMax();

}
