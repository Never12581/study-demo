package com.hzx.sort.radix;

/**
 * @Author: bocai.huang
 * @Descripition: 基数排序
 * @Date: Create in 16:44 2019/2/26
 */
public class RadixSort {

    /**
     * lsd：先排最小位，再依次排序位数大的
     * 按桶排序，从最小位开始排序，排完以后放回到数组中，再排序次高位，最后排序最好味
     */
    public static void lsdSort(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("error input arrays.");
        }
        // O（n）
        int max = getMax(array);
        // 获取位数
        int digit = String.valueOf(max).length();
        //取10个链表的数组
        Node[] buckets = new Node[10];
        ttt(array, buckets, digit, 0);
    }

    /**
     * @param array   数组
     * @param buckets 桶
     * @param digit   当前排序的位
     */
    public static void ttt(int[] array, Node[] buckets, int digit, int currentDigit) {
        if (array == null) {
            throw new IllegalArgumentException("the array is null !");
        }
        if (digit < 0) {
            throw new IllegalArgumentException("digit cannot less than 0!");
            // return;
        }
        if (digit == currentDigit) {
            return;
        }
        int length = array.length;
        int digitNum = (int) Math.pow(10, currentDigit);
        // 全部放入桶中
        for (int i = 0; i < length; i++) {
            // fixme : 此处的计算是耗时操作,需要改进
            int index = array[i] / digitNum % 10;

            // 下述方案更慢
            // int index = 0;
            // String temp = String.valueOf(array[i]);
            // if((temp.length()-currentDigit-1)>0) {
            //     temp = temp.substring(temp.length() - currentDigit - 1, temp.length() - currentDigit);
            //     index = Integer.parseInt(temp);
            // } else {
            //     index = 0 ;
            // }

            // 也慢
            // int index = 0;
            // char[] cTemp = String.valueOf(array[i]).toCharArray();
            // int charIndex = cTemp.length - currentDigit - 1;
            // if (charIndex >= 0) {
            //     index = cTemp[charIndex] - 48;
            // }

            Node node = buckets[index];
            buckets[index] = entryNode(node, array[i]);
        }
        // 再从桶中放入数组

        getArrayFromNodes(buckets, array);

        // digit --
        currentDigit++;

        // node数组清空
        clearNodeArray(buckets);

        ttt(array, buckets, digit, currentDigit);

    }

    /**
     * 获取数组中最大的值
     *
     * @param array
     * @return
     */
    public static int getMax(int[] array) {
        if (array == null) {
            throw new IllegalArgumentException("error input arrays.");
        }
        int max = array[0];
        for (int value : array) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    /**
     * 进入链表
     *
     * @param node
     * @param value
     * @return
     */
    private static Node entryNode(Node node, int value) {
        Node waitToInsert = new Node().setValue(value).setNext(null);
        if (node == null) {
            return waitToInsert;
        }
        Node tempNode = node.getLast();
        if (tempNode == null) {
            tempNode = node.getNext();
        }
        if (tempNode != null) {
            tempNode.setNext(waitToInsert);
        } else {
            node.setNext(waitToInsert);
        }
        node.setLast(waitToInsert);
        return node;
    }

    private static void clearNodeArray(Node[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = null;
        }
    }

    private static void getArrayFromNodes(Node[] nodes, int[] array) {
        int index = 0;
        for (Node node : nodes) {
            Node tempNode = node;
            while (tempNode != null) {
                array[index++] = tempNode.getValue();
                tempNode = tempNode.getNext();
            }
        }
    }

    enum Position {
        /**
         * 从高到低排序
         */
        HIGH_TO_LOW,
        /**
         * 从低到高排序
         */
        LOW_TO_HIGH
    }

    /**
     * 因为每次只分10个桶，所以在大量数据的情况下，形成的链会很长，为了优化，此时将当前链的最后一个链回到头节点
     * fixme：当前数据结构未完善，默认传入节点为首节点
     */
    static class Node {
        private int value;
        private Node next;
        private Node last;

        public int getValue() {
            return value;
        }

        public Node setValue(int value) {
            this.value = value;
            return this;
        }

        public Node getNext() {
            return next;
        }

        public Node setNext(Node next) {
            this.next = next;
            return this;
        }

        public Node getLast() {
            return last;
        }

        public Node setLast(Node last) {
            this.last = last;
            return this;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", next=" + next +
                    ", last=" + last +
                    '}';
        }
    }
}
