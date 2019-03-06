package com.hzx.tree.abstracts;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 22:35 2019/3/5
 */
public interface Tree<T> {

    int size();

    boolean isEmpty();

    void add(T value);

    boolean contains(T value);

    // 移除指定元素
    void remove(T value);

    T findMax();

    T findMin();

    // 移除二分搜索树中的最大值，并将其返回
    T removeMax();

    // 移除二分搜索树中的最小值，并将其返回
    T removeMin();

}
