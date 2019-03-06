package com.hzx.tree.avl;

import com.hzx.tree.binary.BinarySearchTree_MYINE;
import org.junit.Before;
import org.junit.Test;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:35 2019/3/6
 */
public class AVLTreeTest {

    public static BinarySearchTree_MYINE<Integer> binarySearchTreeMYINE = null ;

    @Before
    public void before(){
        binarySearchTreeMYINE = new BinarySearchTree_MYINE<>();
        binarySearchTreeMYINE.add(5);
        binarySearchTreeMYINE.add(2);
        binarySearchTreeMYINE.add(9);
        binarySearchTreeMYINE.add(1);
        binarySearchTreeMYINE.add(3);
        binarySearchTreeMYINE.add(4);
        binarySearchTreeMYINE.add(8);
        binarySearchTreeMYINE.add(6);
        binarySearchTreeMYINE.add(7);
    }

    /**
     * 先序遍历
     */
    @Test
    public void preTraversal() {
        binarySearchTreeMYINE.inOrder();
    }

    @Test
    public void remove(){
        binarySearchTreeMYINE.remove(3);
        binarySearchTreeMYINE.inOrder();
    }

    @Test
    public void contains(){
        System.out.println(binarySearchTreeMYINE.contains(5));
    }

    @Test
    public void findMax(){
        System.out.println(binarySearchTreeMYINE.findMax());
    }

    @Test
    public void findMin(){
        System.out.println(binarySearchTreeMYINE.findMin());
    }

    @Test
    public void removeMax(){
        for(int i = 0; i < binarySearchTreeMYINE.size() ; i++) {
            binarySearchTreeMYINE.removeMax();
        }
    }

    @Test
    public void removeMin(){
        System.out.println(binarySearchTreeMYINE.removeMin());
        System.out.println("====");
        binarySearchTreeMYINE.inOrder();
    }

}
