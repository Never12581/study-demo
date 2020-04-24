package com.hzx.nowcoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * 求解最小公倍数
 * 运行时间：50ms
 *
 * 占用内存：10580k
 */
public class MinimumCommonMultiple {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();
        String[] ss = input.split(" ");
        int first = Integer.parseInt(ss[0]);
        int second = Integer.parseInt(ss[1]);

        List<Integer> listOne = get(first);
        List<Integer> listTwo = get(second);

        int result = 1;
        if (listOne.size() > listTwo.size()) {
            result = first * calcu(listOne, listTwo);

        } else {
            result = second * calcu(listTwo, listOne);
        }
        System.out.println(result);

    }

    private static List<Integer> get(int num) {
        List<Integer> list = new ArrayList<>();
        for (int i = 2; i <= num; i++) {
            if (num % i == 0) {
                list.add(i);
                num = num / i ;
                i = 1 ;
            }
        }
        return list;
    }


    private static int calcu(List<Integer> listOne, List<Integer> listTwo) {
        int result = 1;
        for (int i = 0; i < listTwo.size(); i++) {
            int source = listTwo.get(i);
            if (!listOne.contains(source)) {
                result = result * source;
            }
        }
        return result;
    }
}
