package com.hzx.nowcoder;

import java.util.*;

/**
 * 输入 字符转
 * 按字典顺序排序
 */
public class DictionaryOrder {

    public static void main(String[] args) {
        second();
    }

    public static void second() {
        Scanner sc = new Scanner(System.in);
        int nums = Integer.parseInt(sc.nextLine());
        Map<String, Integer> set = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int length = o1.length() > o2.length() ? o2.length() : o1.length();
                for (int i = 0; i < length; i++) {
                    int i1 = (int) o1.charAt(i);
                    int i2 = (int) o2.charAt(i);
                    if (i1 != i2) {
                        return i1 - i2;
                    }
                }
                return o1.length() - o2.length();
            }
        });
        for (int i = 0; i < nums; i++) {
            String s = sc.nextLine();
            if (set.containsKey(s)) {
                Integer m = set.get(s);
                set.put(s, m + 1);
            } else {
                set.put(s, 1);
            }
        }

        set.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                System.out.println(k);
            }
        });

    }


}
