package com.hzx.test;

public class CharNode {

    Character c;
    CharNode next;

    public Character getC() {
        return c;
    }

    public void setC(Character c) {
        this.c = c;
    }

    public CharNode getNext() {
        return next;
    }

    public void setNext(CharNode next) {
        this.next = next;
    }

    public CharNode(Character c, CharNode next) {
        this.c = c;
        this.next = next;
    }
}
