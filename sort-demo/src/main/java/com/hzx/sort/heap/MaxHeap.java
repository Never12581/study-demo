package com.hzx.sort.heap;

import java.util.List;

/**
 * @author: bocai.huang
 * @create: 2019-09-27 09:46 最大堆 因实现受限，最好传入的是 数组类型的List
 **/
public class MaxHeap<T extends Comparable<T>> {

    /**
     *
     *      数组 arrays 如下 - 下面 的是 下标
     *
     *    |3 |   5   |   1   |   2   |   7   |   13  |
     *    |- |   -   |   -   |   -   |   -   |   -   |
     *    |0 |   1   |   2   |   3   |   4   |   5   |
     *
     *      以层序遍历的形式构建一颗完全二叉树
     *                      3
     *                    /   \
     *                 5        1
     *                /  \     /
     *              2     7   13
     *
     *       此时  根结点元素 为 arrays[0]
     *       其 左 节点 元素 为  arrays[0*2+1] ， 右节点为 arrays[0*2+2]
     *
     *       以 节点 2 为 子节点 节点2 为 arrays[3] ， 其父节点 5 为 arrays[1]
     *       计算方式为 (3-1)/2 = 1
     *       以 节点 7 为 子节点 节点7 为 arrays[4] ， 其父节点 5 为 arrays[1]
     *       计算方式 （4-1)/2 = 1
     *
     *       根据数学归纳法（此处未证明）可得 以下特征：
     *          1. 根据 父节点 index 获取 左子节点下标： index * 2 + 1
     *                                  右子节点下标： index * 2 + 2
     *          2. 根据 子节点 index 获取 父节点下标： (index - 1 ) / 2
     *
     *
     *     最大堆概念：
     *      1. 结构为完全二叉树
     *      2. 父节点比子节点大
     *     ---------------------
     *     最小堆概念：
     *      1. 结构为完全二叉树
     *      2. 父节点比子节点啊小
     *
     *      如何将一个数组转化为一个最大堆？
     *      方案一heapify：
     *          将数据看作是一个完全二叉树，从最后一个  非叶子节点  开始，只要当前非叶子节点极其左右节点满足最大堆的性质
     *      再向前遍历（不满足条件时进行位置的调整，将子节点中最大值与自己交换位置），当所有的非叶子节点及其子节点均
     *      满足最大堆的概念，则当前数组（完全二叉树）满足最大堆的概念
     *      ----------------------------------------------------------------------------------------------
     *      解释为何从"非叶子节点"开始排，以上 完全二叉树 结构来看，叶子节点为：2，7，13 该三个节点单独来看不存在树结构，
     *      也就不存在堆接口，不需要调整。
     *      最后一个非叶子节点为1，该已形成 树结构，需要令其满足 堆接结构，所以需要调整！
     */

    /**
     * 数组
     */
    private List<T> data;

    public MaxHeap(List<T> list) {
        data = list;
        int index = findParentIndex(list.size() - 1);
        for (; index >= 0; index--) {
            swapDown(index);
        }
    }

    public T findMax() {
        return data.get(0);
    }

    public T popMax() {
        T max = data.get(0);
        data.set(0,data.get(data.size()-1));
        data.remove(data.size()-1);
        swapDown(0);
        return max;
    }

    /**
     * 将元素下沉
     */
    private void swapDown(int index) {
        if(data.size() == 0) {
            return;
        }
        if (index < 0) {
            throw new RuntimeException("index illegal! index cannot less than zero");
        }
        if (index > data.size() - 1) {
            throw new RuntimeException("index illegal! index cannot bigger than arrays.size");
        }
        while (true) {
            T parent = data.get(index);
            int leftIndex = findLeftChildIndex(index);
            int rightIndex = findRightChildIndex(index);

            // 不存在左右下标
            /**
             * 当前节点的左子节点下标不存，那么右子节点下标一定不存在，说明其是子节点不需要再下沉了
             */
            if (leftIndex < 0) {
                return;
            }
            /**
             * 如果右子节点下标不存在，仅需要判断左子节点
             *
             * 1.compareTo(2) = -1
             *
             */
            if (rightIndex < 0) {
                if (parent.compareTo(data.get(leftIndex)) < 0) {
                    swap(index, leftIndex);
                    index = leftIndex;
                    continue;
                } else {
                    break;
                }
            }

            /**
             * 判断出三者最大值
             */
            int maxIndex = 0;
            if (data.get(leftIndex).compareTo(data.get(rightIndex)) < 0) {
                maxIndex = rightIndex;
            } else {
                maxIndex = leftIndex;
            }

            if (data.get(index).compareTo(data.get(maxIndex)) < 0) {
                swap(index, maxIndex);
            }
            index = maxIndex;
        }

    }

    /**
     * 将元素上浮
     */
    private void swapUp(int index) {
        if(data.size() == 0) {
            return;
        }
        if (index < 0) {
            throw new RuntimeException("index illegal! index cannot less than zero");
        }
        if (index > data.size() - 1) {
            throw new RuntimeException("index illegal! index cannot bigger than arrays.size");
        }
        while (true) {
            if (index == 0) {
                break;
            }
            int parentIndex = findParentIndex(index);
            if (data.get(index).compareTo(data.get(parentIndex)) < 0) {
                swap(index, parentIndex);
                index = parentIndex;
            } else {
                break;
            }
        }
    }

    /**
     * 交换该下标对应的两个值
     */
    private void swap(int index, int index2) {
        if (index < 0
            || index > data.size() - 1
            || index2 < 0
            || index2 > data.size() - 1) {
            throw new RuntimeException("index illegal! index cannot less than zero");
        }
        T temp = data.get(index);
        data.set(index, data.get(index2));
        data.set(index2, temp);
    }

    private int findParentIndex(int index) {
        if (index < 0) {
            throw new RuntimeException("index illegal!");
        }
        if (index == 0) {
            throw new RuntimeException("this node do not have a parent node!");
        }
        return (index - 1) / 2;
    }

    private T findParent(int index) {

        return data.get(findParentIndex(index));
    }

    private int findLeftChildIndex(int index) {
        if (index < 0) {
            throw new RuntimeException("index illegal! index cannot less than zero");
        }
        if (index > data.size() - 1) {
            throw new RuntimeException("index illegal! index cannot bigger than arrays.size");
        }
        index = index * 2 + 1;
        if (index > data.size() - 1) {
            return -1;
        }
        return index;
    }

    private T findLeftChild(int index) {
        index = findLeftChildIndex(index);
        if (index < 0) {
            return null;
        }
        return data.get(index);
    }

    private int findRightChildIndex(int index) {
        if (index < 0) {
            throw new RuntimeException("index illegal! index cannot less than zero");
        }
        if (index > data.size() - 1) {
            throw new RuntimeException("index illegal! index cannot bigger than arrays.size");
        }
        index = index * 2 + 2;
        if (index > data.size() - 1) {
            return -1;
        }
        return index;
    }

    private T findRightChild(int index) {
        index = findRightChildIndex(index);
        if (index < 0) {
            return null;
        }
        return data.get(index);
    }

}
