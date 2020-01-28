package com.hzx.medium;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author: bocai.huang
 * @create: 2020-01-20 17:11
 **/
public class LengthOfLongestSubstring {

    public static void main(String[] args) {
//        System.out.println(lengthOfLongestSubstring("pwwkew"));
//        System.out.println(lengthOfLongestSubstring("abcabcbb"));
        System.out.println(lengthOfLongestSubstring("tmmzuxt"));
    }

    public static int lengthOfLongestSubstring(String s) {
        if (s == null || "".equals(s)) {
            return 0;
        }

        char[] chars = s.toCharArray();
        int start = 0;
        TreeSet<Integer> lengthSet = new TreeSet<>();

        Map<Character,Integer> map = new HashMap<>();

        char c;
        for (int i = 0; i < chars.length; i++) {
            c = chars[i];
            if (map.containsKey(c)) {
                start = i;
            } else {
                System.out.println("当前字符："+c);
                System.out.println("当前长度："+(i - start +1 ));
                lengthSet.add(i - start +1 );
            }
            map.put(c,i);
        }

        return lengthSet.last();
    }

}
