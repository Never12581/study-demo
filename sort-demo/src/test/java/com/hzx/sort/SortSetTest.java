package com.hzx.sort;

import org.junit.Test;

import java.util.*;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 11:14 2019/2/20
 */
public class SortSetTest {

    @Test
    public void test() {
        // SortedSet<Integer> sortedSet = new TreeSet<>();
        //
        // sortedSet.add(7);
        // sortedSet.add(6);
        // sortedSet.add(8);
        // sortedSet.add(5);
        // sortedSet.add(9);
        //
        // PriorityQueue<Integer> pq = new PriorityQueue<>(sortedSet);

        // List<Integer> list = new ArrayList<>();
        // list.add(7);
        // list.add(6);
        // list.add(8);
        // list.add(5);
        // list.add(9);
        // PriorityQueue<Integer> pq = new PriorityQueue<>(list);
        //
        // Object[] obs = pq.toArray();
        // for (Object ob : obs) {
        //     System.out.println(ob);
        // }

        List<Integer> list = new ArrayList<>();
        list.add(2);

        for(int i = 0 ; i < list.size()  ; i++) {
            if(list.get(i)==2) {
                list.remove(list.get(i));
            }
        }

    }

}
