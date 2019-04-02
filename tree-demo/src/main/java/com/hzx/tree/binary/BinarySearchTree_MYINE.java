package com.hzx.tree.binary;

import com.hzx.tree.abstracts.BalancedTree;

/**
 * @Author: bocai.huang
 * @Descripition: 非递归版本，当前为残缺版本，因为并为加入自平衡操作
 * @Date: Create in 14:07 2019/3/6
 */
public class BinarySearchTree_MYINE<T extends Comparable<T>> implements BalancedTree<T> {

    // 节点高度：叶子节点高度为1 ，非叶子节点为深度最深的高度
    // 平衡因子：当前左，右子树高度差

    private Node root;
    private int size;

    public BinarySearchTree_MYINE() {
        this.root = null;
        size = 0;
    }

    public BinarySearchTree_MYINE(Node root) {
        this.root = root;
        this.size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void add(T value) {
        add(root, value);
    }

    @Override
    public boolean contains(T value) {
        return contains(root, value);
    }

    @Override
    public void remove(T value) {
        remove(root, value);
    }

    @Override
    public T findMax() {
        return findMax(root).value;
    }

    @Override
    public T findMin() {
        return findMin(root).value;
    }

    @Override
    public T removeMax() {
        removeMax(root);
        return null;
    }

    @Override
    public T removeMin() {
        Node retNode = removeMin(root);
        if (retNode != null) {
            return retNode.value;
        }
        return null;
    }

    @Override
    public boolean isBalanced() {
        return false;
    }

    // 中序遍历，排序后的结果
    public void inOrder() {
        inOrder(root);
    }

    private void inOrder(Node node) {
        if (node != null) {
            inOrder(node.left);
            System.out.println(node.value + ",");
            inOrder(node.right);
        }
    }

    private Node add(Node node, T value) {
        Node waitToInsert = new Node(value);
        if (node == null) {
            size++;
            return waitToInsert;
        }

        Node tempNode = node;
        while (tempNode != null && tempNode.value.compareTo(value) != 0) {
            if (tempNode.value.compareTo(value) > 0) {
                if (tempNode.left == null) {
                    tempNode.left = waitToInsert;
                }
                tempNode = tempNode.left;
            } else {
                if (tempNode.right == null) {
                    tempNode.right = waitToInsert;
                }
                tempNode = tempNode.right;
            }

        }
        size++;
        return node;
    }

    private boolean contains(Node node, T value) {
        if (node.value.compareTo(value) == 0) {
            return true;
        } else if (node.value.compareTo(value) > 0) {
            return contains(node.left, value);
        } else {
            /*if (node.value.compareTo(value) < 0)*/
            return contains(node.right, value);
        }
    }

    private Node findMax(Node node) {
        if (node == null) {
            return null;
        }
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private Node findMin(Node node) {
        if (node == null) {
            return null;
        }
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    // 删除以node为根节点的子树中的最大值，并返回被删除的节点
    private Node removeMax(Node node) {
        //做法与min相似
        Node currentNode = node;
        Node parentNode = node;
        if (currentNode == null) {
            return null;
        }

        while (currentNode.right != null) {
            parentNode = currentNode;
            currentNode = currentNode.right;
        }
        parentNode.right = currentNode.left;
        currentNode.left = null;
        size--;
        return currentNode;
    }

    // 在以当前节点为跟节点的子树中，删除其中最小值
    private Node removeMin(Node node) {
        //做法与min相似
        Node currentNode = node;
        Node parentNode = node;
        if (currentNode == null) {
            return null;
        }

        while(currentNode.left!=null){
            parentNode=currentNode;
            currentNode=currentNode.left;
        }
        parentNode.left=currentNode.right;
        currentNode.right=null;
        size--;
        return currentNode;
    }

    // 在以node为根节点的子树中删除value元素
    private Node remove(Node node, T value) {

        Node delNode = null;
        boolean flag = false;

        while (node.value.compareTo(value) != 0) {
            if (node.value.compareTo(value) > 0) {

                if (node.left.value.compareTo(value) == 0) {
                    delNode = node.left;
                    flag = false;
                    break;
                }

                node = node.left;

            } else {

                if (node.right.value.compareTo(value) == 0) {
                    delNode = node.right;
                    flag = true;
                    break;
                }

                node = node.right;
            }
        }

        // node 即为当前元素的父亲元素
        // delNode 即为当前需要被删除的元素
        // 基本做法：寻找到当前被删除的元素的 右子树中，最大的一个元素，取代当前元素
        Node tempNode = removeMax(delNode);
        tempNode.left = delNode.left;
        tempNode.right = delNode.right;
        if (flag) {
            node.right = tempNode;
        } else {
            node.left = tempNode;
        }
        return delNode;
    }

    private class Node {
        public T value;
        public Node left, right;
        // 节点高度：叶子节点高度为1 ，非叶子节点为深度最深的高度
        public int height;

        public Node(T value) {
            this.value = value;
            left = right = null;
            // 默认新节点，为叶子节点，高度为1
            height = 1;
        }

    }

}
