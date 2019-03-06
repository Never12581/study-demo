package com.hzx.tree.binary;

import com.hzx.tree.abstracts.Tree;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 17:39 2019/3/1
 */
public class BinarySearchTree<T extends Comparable<T>> implements Tree<T> {

    private Node root;
    private int size;

    public BinarySearchTree() {
        root = null;
        size = 0;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    // 向二分搜索树中添加新元素
    @Override
    public void add(T value) {
        root = add(root, value);
    }

    @Override
    public boolean contains(T value) {
        return contains(root, value);
    }

    // 在二分搜索树中移除元素
    @Override
    public void remove(T value) {
        root = remove(root, value);
    }

    @Override
    public T findMax() {
        if (size == 0) {
            throw new IllegalArgumentException("The binarySearchTree is empty . Cannot find the max value!");
        }
        return findMax(root).value;
    }

    @Override
    public T findMin() {
        if (size == 0) {
            throw new IllegalArgumentException("The binarySearchTree is empty . Cannot find the min value!");
        }
        return findMin(root).value;
    }

    @Override
    public T removeMax() {
        if (size == 0) {
            throw new IllegalArgumentException("The binarySearchTree is empty . Cannot remove the max value!");
        }
        Node retNode = removeMax(root);
        if (retNode != null) {
            return retNode.value;
        }
        return null;
    }

    @Override
    public T removeMin() {
        if (size == 0) {
            throw new IllegalArgumentException("The binarySearchTree is empty . Cannot find the min value!");
        }
        Node retNode = removeMin(root);
        if (retNode != null) {
            return retNode.value;
        }
        return null;
    }

    // 向以node为节点的二分搜索树中添加元素value
    private Node add(Node node, T value) {
        if (node == null) {
            size++;
            return new Node(value);
        } else if (node.value.compareTo(value) > 0) {
            node.left = add(node.left, value);
        } else if (node.value.compareTo(value) < 0) {
            node.right = add(node.right, value);
        }
        return node;
    }

    private boolean contains(Node node, T value) {
        if (node == null) {
            return false;
        } else if (node.value.compareTo(value) == 0) {
            return true;
        } else if (node.value.compareTo(value) < 0) {
            return contains(node.left, value);
        } else {
            // condition : node.value.compareTo(value) > 0
            return contains(node.right, value);
        }
    }

    /**
     * 5
     * 2               9
     * 1      3        7       12
     * 此时要删除5这个元素，需要调整二分搜索树
     * 原则：找到比5大的元素中最小的一个，将其删除，其值赋给5这个节点的node
     */
    // 在向以node为跟节点的二分搜索树中移除元素
    private Node remove(Node node, T value) {

        if (node == null) {
            return null;
        } else if (node.value.compareTo(value) == 0) {

            Node retNode = null;

            // 如果其左子树为空，则直接将其右子树节点返回
            if (node.left == null) {
                size--;
                retNode = node.right;
                node.right = null;
            }
            // 如果其右子树为空，则直接将其左子树节点返回
            else if (node.right == null) {
                size--;
                retNode = node.left;
                node.left = null;
            }
            // 如果左右子树均不为空，
            else /*if(node.left != null && node.right != null)*/ {
                // 一般的处理方法是从右子树中查询出最小值，将当前最小值作为节点进行返回
                retNode = findMin(node.right);
                retNode.right = removeMin(node.right);
                retNode.left = node.left;
                node.left = node.right = null;
            }

            return retNode;
        } else if (node.value.compareTo(value) < 0) {
            // 如果当前值大于当前节点值
            node.right = remove(node.right, value);
            return node;
        } else {
            // 当前节点值 小于当前value
            node.left = remove(node.left, value);
            return node;
        }
    }

    // 查询以node为节点的二分搜索树中节点的最小值
    private Node findMin(Node node) {
        if (node == null) {
            // throw new IllegalArgumentException("node is null ,cannot find min value !");
            return null;
        } else if (node.left == null) {
            return node;
        } else {
            return findMin(node.left);
        }
    }

    // 查询以node为节点的二分搜索树中节点的最大值
    private Node findMax(Node node) {
        if (node == null) {
            // throw new IllegalArgumentException("node is null ,cannot find max value !");
            return null;
        } else if (node.right == null) {
            return node;
        } else {
            return findMax(node.right);
        }
    }

    // 删除以node为跟节点的树中的最小值，返回新的二分搜索树的根
    private Node removeMin(Node node) {
        if (node == null) {
            // throw new IllegalArgumentException("cannot remove min value in a null node !");
            return null;
        }
        if (node.left == null) {
            Node retNode = node.right;
            node.right = null;
            size--;
            return retNode;
        } else {
            node.left = removeMin(node.left);
            return node;
        }
    }

    // 删除以node为跟节点树中的最大值
    private Node removeMax(Node node) {
        if (node == null) {
            // throw new IllegalArgumentException("cannot remove max value in a null node !");
            return null;
        }
        if (node.right == null) {
            Node retNode = node.left;
            node.left = null;
            size--;
            return retNode;
        } else {
            node.right = removeMax(node.right);
            return node;
        }
    }

    // 前序遍历
    public void preOrder() {
        preOrder(root);
    }

    private void preOrder(Node node) {
        if (node != null) {
            System.out.println(node.value + ",");
            preOrder(node.left);
            preOrder(node.right);
        }
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

    // 后序遍历
    public void postOrder() {
        postOrder(root);
    }

    private void postOrder(Node node) {
        if (node != null) {
            postOrder(node.left);
            postOrder(node.right);
            System.out.println(node.value + ",");
        }
    }

    // 层序遍历
    // 1、将当前节点放入队列中
    // 2、将当前节点从队列中取出
    // 3、将当前节点的左子树，右子树分别入队
    // 4、重复2和3

    private class Node {
        public T value;
        public Node left, right;

        public Node(T value) {
            this.value = value;
            left = null;
            right = null;
        }
    }
}
