package com.hzx.easy;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:37 2019/5/10
 * @Question: 给定一个数组与一个target值，返回一组任意两数和为该target值的数组
 */
public class TwoSum {

    /**
     * for example：
     * nums = [1,2,3,4,5,6,7]
     * target = 7
     * return [3,4]
     */

    public static int[] _two(int[] nums, int target) {
        Map<Integer,Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i],i);
        }

        Set<Map.Entry<Integer,Integer>> entries = map.entrySet();

        Iterator<Map.Entry<Integer,Integer>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer,Integer> entry = iterator.next();

            Integer key = entry.getKey();
            Integer value = entry.getValue();

            Integer newKey = target - key;
            Integer newValue = map.get(newKey);
            if(newValue != null) {
                int[] result = new int[2];
                result[0] = value;
                result[1] = newValue;
                return result;
            }

        }
        return null;
    }

    public static int[] _one(int[] nums, int target) {
        int i, j = 0, sum;
        int length = nums.length;

        TYPE:
        for (i = 0; i < length; i++) {
            for (j = i + 1; j < length; j++) {
                sum = nums[i] + nums[j];
                if (sum == target) {
                    break TYPE;
                }
            }
        }

        int[] result = new int[2];
        result[0] = nums[i];
        result[1] = nums[j];

        return result;
    }

    public static void main(String[] args) {
        int[] nums = { 3, 3, 11, 15 };
        int target = 6;

        int[] result = _two(nums, target);

        for (int r : result) {
            System.out.println(r);
        }

    }

}
