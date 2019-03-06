package com.hzx.tree.avl;

import org.junit.Before;
import org.junit.Test;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 14:35 2019/3/6
 */
public class AVLTreeTest {

    public static AVLTree<Integer> avlTree = null ;

    @Before
    public void before(){
        avlTree = new AVLTree<>();
        avlTree.add(5);
        avlTree.add(2);
        avlTree.add(9);
        avlTree.add(1);
        avlTree.add(3);
        avlTree.add(4);
        avlTree.add(8);
        avlTree.add(6);
        avlTree.add(7);
    }

    /**
     * 先序遍历
     */
    @Test
    public void preTraversal() {
        avlTree.inOrder();
    }

    @Test
    public void remove(){
        avlTree.remove(3);
        avlTree.inOrder();
    }

    @Test
    public void contains(){
        System.out.println(avlTree.contains(5));
    }

    @Test
    public void findMax(){
        System.out.println(avlTree.findMax());
    }

    @Test
    public void findMin(){
        System.out.println(avlTree.findMin());
    }

    @Test
    public void removeMax(){
        for(int i = 0 ; i < avlTree.size() ; i++) {
            avlTree.removeMax();
        }
    }

    @Test
    public void removeMin(){
        System.out.println(avlTree.removeMin());
        System.out.println("====");
        avlTree.inOrder();
    }

}
