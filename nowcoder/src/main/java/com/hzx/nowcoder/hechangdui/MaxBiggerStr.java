package com.hzx.nowcoder.hechangdui;

/**
 * 最长递增子序列
 */
public class MaxBiggerStr {

    public static void main(String[] args) {
        String s = "725389";
        System.out.println(calcu(s));
    }

    /**
     * 求证最长递增子序列 的长度以及 序列
     * 725389  --> 4
     * 2589
     * 2389
     * <p>
     * 7   2   5   3   8   9
     * 0   0   0   0   0   0   0
     * 2    0
     * 3    0
     * 5    0
     * 7    0
     * 8    0
     * 9    0
     */
    public static int calcu(String str) {
        int[] common = new int[str.length()+1];
        for (int i = 1; i <= str.length(); i++) {
            common[i] = 1;
            int max = common[i];
            for (int k = 1; k <= i; k++) {
                if (str.charAt(i) > str.charAt(k)) {
                    max = max(common[k]+1,max);
                    common[i] = max ;
                }
            }

        }
        return common[str.length()];
    }

    public static int max(int a, int b) {
        return a > b ? a : b;
    }

}
