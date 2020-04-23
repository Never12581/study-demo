package com.hzx.nowcoder;

import java.util.*;

/**
 * 字符串排序
 * https://www.nowcoder.com/practice/5190a1db6f4f4ddb92fd9c365c944584?tpId=37&tqId=21249&rp=0&ru=/ta/huawei&qru=/ta/huawei/question-ranking
 * <p>
 * 规则 1 ：英文字母从 A 到 Z 排列，不区分大小写。
 * 如，输入： Type 输出： epTy
 * <p>
 * 规则 2 ：同一个英文字母的大小写同时存在时，按照输入顺序排列。
 * 如，输入： BabA 输出： aABb
 * <p>
 * 规则 3 ：非英文字母的其它字符保持原来的位置。
 * 如，输入： By?e 输出： Be?y
 */
public class SortString {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String s = sc.nextLine();
            Set<Integer> set = new HashSet<>();
            Map<Integer, List<Character>> map = new TreeMap<>();

            for (int i = 0; i < s.length(); i++) {
                char c = s.charAt(i);
                int key = 0;
                if (c >= 'a' && c <= 'z') {
                    key = (int) c - 32;
                    if (map.containsKey(key)) {
                        map.get(key).add(c);
                    } else {
                        List<Character> list = new ArrayList<>();
                        list.add(c);
                        map.put(key, list);
                    }
                } else if (c >= 'A' && c <= 'Z') {
                    key = (int) c;
                    if (map.containsKey(key)) {
                        map.get(key).add(c);
                    } else {
                        List<Character> list = new ArrayList<>();
                        list.add(c);
                        map.put(key, list);
                    }
                } else {
                    set.add(i);
                }
            }

            List<Character> list = new ArrayList<>();
            map.forEach((k, v) -> {
                list.addAll(v);
            });

            StringBuilder sb = new StringBuilder();

            int index = 0;
            for (int i = 0; i < s.length(); i++) {
                if (set.contains(i)) {
                    sb.append(s.charAt(i));
                } else {
                    sb.append(list.get(index));
                    index++;
                }
            }
            System.out.println(sb.toString());
        }
    }
}
