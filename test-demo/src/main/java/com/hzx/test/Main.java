package com.hzx.test;

public class Main {

    /*
    删除给定字符串中 所有字典连续递增 的字符，字符串中仅包含小写字母
    如：ecdfgba -> efgba -> ba
     */
    public static void main(String[] args) {
        String s = "ecdfgba";
        s = test(s);
        System.out.println(s);
    }

    public static String test(String source) {
        CharNodeList list = build(source);
        remove(list);
        StringBuilder stringBuilder = new StringBuilder();
        CharNode head = list.head;
        while (head.next != null) {
            stringBuilder.append(head.next.c);
            head = head.next;
        }
        return stringBuilder.toString();
    }

    // 将字符串构造成 charNode 链表
    public static CharNodeList build(String source) {
        if (null == source || "".equals(source)) {
            return null;
        }
        CharNodeList charNodeList = new CharNodeList();
        CharNode head = new CharNode(null, null);
        CharNode currentNode = head;
        for (int i = 0; i < source.length(); i++) {
            // 普通的构造逻辑
            CharNode temp = new CharNode(source.charAt(i), null);
            currentNode.next = temp;
            currentNode = temp;
        }
        charNodeList.setHead(head);
        charNodeList.setEnd(currentNode);
        return charNodeList;
    }

    // 清除 连续递增的 字符
    // 从当前节点 向后遍历 如果遇到 连续递增的，则将子序列删除
    // 如果单次遍历中，均未出现删除操作，则证明 已经删除成功
    // ecdfgbab
    public static void remove(CharNodeList charNodeList) {
        if (null == charNodeList) {
            return;
        }
        CharNode head = charNodeList.head;
        // 当前不需要处理的节点
        CharNode parent = head;
        CharNode currentNode = head.next;

        while (currentNode.next != null) {
            if ( ((int) currentNode.c + 1) != (int) currentNode.next.c ) {
                if (parent.next == currentNode) {
                    parent = currentNode;
                    currentNode = currentNode.next;
                } else {
                    parent.next = currentNode.next;
                    parent = head;
                    currentNode = head.next;
                }
            } else {
                currentNode = currentNode.next;
            }
        }
        // 临界状态处理
        if (parent.next != currentNode) {
            parent.next = currentNode.next;
        }
    }


}
