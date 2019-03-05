package com.hzx.tree.binary;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 17:39 2019/3/1
 */
public class BinarySortTree<T> {

    private T value;
    private BinarySortTree leftChild;
    private BinarySortTree rightChild;

    public T getValue() {
        return value;
    }

    public BinarySortTree<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public BinarySortTree getLeftChild() {
        return leftChild;
    }

    public BinarySortTree<T> setLeftChild(BinarySortTree leftChild) {
        this.leftChild = leftChild;
        return this;
    }

    public BinarySortTree getRightChild() {
        return rightChild;
    }

    public BinarySortTree<T> setRightChild(BinarySortTree rightChild) {
        this.rightChild = rightChild;
        return this;
    }

    @Override
    public String toString() {
        return "BinarySortTree{" +
                "value=" + value +
                ", leftChild=" + leftChild +
                ", rightChild=" + rightChild +
                '}';
    }
}
