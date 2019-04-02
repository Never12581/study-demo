package com.hzx.tree;

import com.hzx.tree.abstracts.Tree;
import com.hzx.tree.avl.AVLTree;
import com.hzx.tree.binary.BinarySearchTree;
import com.hzx.tree.binary.BinarySearchTree_MYINE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@SpringBootApplication
@RestController
public class TreeApplication {

    public static void main(String[] args) {
        SpringApplication.run(TreeApplication.class, args);
    }

    @GetMapping(value = "test")
    public void test() {
        final int NUM = 1000000;
        List<Integer> list = new ArrayList<>(NUM);

        Random random = new Random();

        for (int i = 0; i < NUM; i++) {
            list.add(random.nextInt(Integer.MAX_VALUE));
        }

        System.out.println("==================递归二叉搜索树=================");
        Tree<Integer> binarySearchTree = new BinarySearchTree<>();
        for (int i = 0; i < NUM; i++) {
            binarySearchTree.add(list.get(i));
        }
        long start = System.currentTimeMillis();
        System.out.println("递归二叉树的size:"+binarySearchTree.size());
        for (int i = 0; i < binarySearchTree.size(); i++) {
            binarySearchTree.removeMax();
        }
        System.out.println("花费时间：" + (System.currentTimeMillis() - start));
        // ((BinarySearchTree<Integer>) binarySearchTree).inOrder();
        System.out.println("递归二叉树的size:"+binarySearchTree.size());

        System.out.println("=================非递归二叉搜索树=================");
        Tree<Integer> avlTree = new BinarySearchTree_MYINE<>();
        for (int i = 0; i < NUM; i++) {
            avlTree.add(list.get(i));
        }
        System.out.println("非递归二叉树的size："+avlTree.size());
        start = System.currentTimeMillis();
        for (int i = 0; i < avlTree.size(); i++) {
            avlTree.removeMax();
        }
        System.out.println("花费时间：" + (System.currentTimeMillis() - start));
        ((BinarySearchTree_MYINE<Integer>) avlTree).inOrder();
        System.out.println("非递归二叉树的size："+avlTree.size());
    }

}
