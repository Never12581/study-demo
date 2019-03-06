package com.hzx.tree.binary;

import org.junit.Before;
import org.junit.Test;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 18:05 2019/3/1
 */
public class BinaryTreeTest {

    public static BinarySearchTree<Integer> binarySearchTree = null ;

    @Before
    public void before(){
        binarySearchTree = new BinarySearchTree<>();
        binarySearchTree.add(5);
        binarySearchTree.add(2);
        // binarySearchTree.add(9);
        // binarySearchTree.add(1);
        // binarySearchTree.add(3);
        // binarySearchTree.add(4);
        // binarySearchTree.add(8);
        // binarySearchTree.add(6);
        // binarySearchTree.add(7);
    }

    /**
     * 先序遍历
     */
    @Test
    public void preTraversal() {
        binarySearchTree.inOrder();
    }

    @Test
    public void remove(){
        binarySearchTree.remove(3);
        binarySearchTree.inOrder();
    }

    @Test
    public void contains(){
        System.out.println(binarySearchTree.contains(5));
    }

    @Test
    public void findMax(){
        System.out.println(binarySearchTree.findMax());
    }

    @Test
    public void removeMax(){
        for (int i = 0 ; i < 2 ; i++) {
            System.out.println(binarySearchTree.removeMax());
        }
    }

}
