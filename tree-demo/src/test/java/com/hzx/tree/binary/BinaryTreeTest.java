package com.hzx.tree.binary;

import org.junit.Before;
import org.junit.Test;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 18:05 2019/3/1
 */
public class BinaryTreeTest {

    public static BinarySortTree<Integer> binarySortTree = null ;

    @Before
    public void before(){
        binarySortTree = new BinarySortTree<Integer>().setValue(2);
        binarySortTree
                .setLeftChild(new BinarySortTree().setValue(7).setLeftChild(new BinarySortTree().setValue(5)).setRightChild(new BinarySortTree().setValue(6)));
        binarySortTree
                .setRightChild(new BinarySortTree().setValue(17).setLeftChild(new BinarySortTree().setValue(15)).setRightChild(new BinarySortTree().setValue(16)));
    }

    /**
     * 先序遍历
     */
    @Test
    public void preTraversal() {
        System.out.println(binarySortTree);
    }

}
