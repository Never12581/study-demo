package com.hzx.nowcoder.hechangdui;

/**
 * 最长公共子序列
 * "abcde"
 * "cacbcdaaa"
 * --> "abcd"
 * <p>
 * https://www.sohu.com/a/339112354_818692
 * https://www.cnblogs.com/Lee-yl/p/9975827.html
 */
public class MaxSameStr {

    public static void main(String[] args) {
        lcs("aaasdfas", "asdf");
    }

    public static void lcs(String str1, String str2) {
        int[][] common = new int[str1.length() + 1][str2.length() + 1];

        for (int i = 1; i <= str1.length(); i++) {
            common[i][1] = common[i - 1][1];
            for (int k = 1; k <= str2.length(); k++) {
                if (str1.charAt(i - 1) == str2.charAt(k - 1)) {
                    common[i][k] = common[i - 1][k - 1] + 1;
                } else {
                    common[i][k] = max(common[i - 1][k], common[i][k - 1]);
                }
            }
        }
        System.out.println(common[str1.length()][str2.length()]);


        for (int i = 0; i <= str1.length(); i++) {
            for (int k = 0; k <= str2.length(); k++) {
                System.out.print(common[i][k] + "\t");
            }
            System.out.println();
        }
    }

    public static int max(int a, int b) {
        if (a > b) {
            return a;
        } else {
            return b;
        }
    }

}
