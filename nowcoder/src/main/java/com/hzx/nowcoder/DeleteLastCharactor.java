package com.hzx.nowcoder;

import java.util.*;

public class DeleteLastCharactor {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            Map<String, Integer> map = new HashMap<>();
            for (int i = 0; i < line.length(); i++) {
                String s = line.substring(i, i + 1);
                if (map.containsKey(s)) {
                    int count = map.get(s) + 1 ;
                    map.put(s, count);
                } else {
                    map.put(s, 1);
                }
            }

            int min = 0;
            boolean isfirst = true;
            for (int i : map.values()) {
                if (isfirst) {
                    min = i;
                    isfirst = false;
                }
                if (i < min) {
                    min = i;
                }
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < line.length(); i++) {
                String s = line.substring(i, i + 1);
                if (map.get(s) > min) {
                    sb.append(s);
                }
            }

            System.out.println(sb.toString());

        }
    }
}
