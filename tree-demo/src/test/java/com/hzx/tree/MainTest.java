package com.hzx.tree;

import java.util.HashSet;
import java.util.Set;

/**
 * @author: bocai.huang
 * @create: 2019-10-29 16:23
 **/
public class MainTest {

    public static void main(String[] args) {
        Set<String> set = new HashSet<>();
        set.add("1");
        set.add("2");
        set.add("3");
//        set.add(null);

        System.out.println(set.contains(null));
    }

}
