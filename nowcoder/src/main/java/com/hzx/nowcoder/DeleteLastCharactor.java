package com.hzx.nowcoder;

import java.util.*;

/**
 * https://www.nowcoder.com/practice/05182d328eb848dda7fdd5e029a56da9?tpId=37&tqId=21246&tPage=2&rp=&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking
 * 删除字符串中出现次数最少的字符
 * <p>
 * 题目描述
 * 实现删除字符串中出现次数最少的字符，若多个字符出现次数一样，则都删除。输出删除这些单词后的字符串，字符串中其它字符保持原来的顺序。
 * 注意每个输入文件有多组输入，即多个字符串用回车隔开
 * 输入描述:
 * 字符串只包含小写英文字母, 不考虑非法输入，输入的字符串长度小于等于20个字节。
 * <p>
 * 输出描述:
 * 删除字符串中出现次数最少的字符后的字符串。
 */
public class DeleteLastCharactor {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        while (scan.hasNextLine()) {
            String str = scan.nextLine();
            if (str.length() > 20) {
                continue;
            }
            int[] max = new int[26];
            char[] ch = str.toCharArray();
            int min = Integer.MAX_VALUE;
            for (int i = 0; i < ch.length; i++) {
                max[ch[i] - 'a']++;
                min = min > max[ch[i] - 'a'] ? max[ch[i] - 'a'] : min;
            }


            for (int i = 0; i < max.length; i++) {
                if (max[i] == min) {

                    str = str.replaceAll(String.valueOf((char) (i + 97)), "");
                }
            }
            System.out.println(str);
        }

    }
}
