package com.hzx.nowcoder;

import com.alibaba.fastjson.JSON;
import lombok.Data;
import lombok.ToString;

import java.util.*;

/**
 * 购物单
 */
public class ShoppingList {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        String[] ss = s.split(" ");
        // 总共的钱
        int totalPrice = Integer.parseInt(ss[0]);
        // 总共的商品数量
        int totalCount = Integer.parseInt(ss[1]);
        // 构造商品列表
        Map<Integer, Good> map = new TreeMap<>();
        for (int i = 1; i <= totalCount; i++) {
            s = sc.nextLine();
            ss = s.split(" ");
            Good good = new Good();
            good.index = i ;
            good.price = Integer.parseInt(ss[0]);
            good.priority = Integer.parseInt(ss[1]);
            good.master = Integer.parseInt(ss[2]);
            if (map.containsKey(good.master)) {
                map.get(good.master).goodList.add(good);
            } else {
                map.put(i, good);
            }
        }

    }

    @ToString
    @Data
    public static class Good {
        private int index ;
        private int price;
        private int priority;
        private int master;
        private List<Good> goodList = new ArrayList<>();
    }

}
