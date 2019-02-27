package com.hzx.sort.bucket;

import java.util.Objects;

/**
 * @Author: bocai.huang
 * @Descripition: 从小到大排序
 * @Date: Create in 14:20 2019/2/26
 */
public class BucketSort {

    public static void sort(int[] array) {

        if (array == null || array.length <= 2) {
            return ;
        }

        int length = array.length;
        int min = array[0];
        int max = array[0];
        for (int i = 0; i < length; i++) {
            if (array[i] <= min) {
                min = array[i];
            }
            if (array[i] >= max) {
                max = array[i];
            }
        }

        Node[] nodes = new Node[length + 1];
        double meanDeviation = ((max - min) / 1.0) / length;

        for (int i = 0; i < length; i++) {
            int index = (int) Math.floor((array[i] - min) / meanDeviation);
            Node node = nodes[index];
            nodes[index] = setNodeValue(node, array[i]);
        }
        getArrayFromNodes(nodes,array);
        return ;
    }

    private static Node setNodeValue(Node node, int value) {
        Node waitToInsertNode = new Node().setPost(null).setPre(null).setValue(value);
        if (Objects.isNull(node)) {
            return waitToInsertNode;
        }

        // 原先的node不为空 则判断值
        Node tempNode = node;
        while (!Objects.isNull(tempNode)) {
            if (tempNode.getValue() >= value) {
                // 做交换
                Node beforeNode = tempNode.getPre();
                if (!Objects.isNull(beforeNode)) {
                    beforeNode.setPost(waitToInsertNode);
                    waitToInsertNode.setPre(beforeNode);
                }
                waitToInsertNode.setPost(tempNode);
                tempNode.setPre(waitToInsertNode);
                break;
            }else
            if(Objects.isNull(tempNode.getPost()) && tempNode.getValue() < value) {
                tempNode.setPost(waitToInsertNode);
                waitToInsertNode.setPre(tempNode);
                break;
            }
            tempNode = tempNode.getPost();
        }

        return node;

    }

    private static void getArrayFromNodes(Node[] nodes, int[] array) {
        int index = 0;
        for (int i = 0; i < nodes.length; i++) {
            Node tempNode = nodes[i];
            while (!Objects.isNull(tempNode)) {
                array[index++] = tempNode.getValue();
                tempNode = tempNode.getPost();
            }
        }
    }

    /**
     * 因为普通的桶排序，数据足够分散，所以链不会太长，所以效率还可以
     * 如果链过长，则会影响效率
     */
    static class Node {
        private int value;
        private Node pre;
        private Node post;

        public int getValue() {
            return value;
        }

        public Node setValue(int value) {
            this.value = value;
            return this;
        }

        public Node getPre() {
            return pre;
        }

        public Node setPre(Node pre) {
            this.pre = pre;
            return this;
        }

        public Node getPost() {
            return post;
        }

        public Node setPost(Node post) {
            this.post = post;
            return this;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", post=" + post +
                    '}';
        }
    }
}
