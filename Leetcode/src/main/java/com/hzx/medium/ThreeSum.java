package com.hzx.medium;

import java.util.*;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:53 2019/12/30
 */
public class ThreeSum {

    public static void main(String[] args) {
        int[] nums = new int[] { -4, -2, -2, -2, 0, 1, 2, 2, 2, 3, 3, 4, 4, 6, 6 };
        ThreeSum three = new ThreeSum();
        for (List<Integer> list : three.threeSum(nums)) {
            System.out.println(list);
        }
    }

    public List<List<Integer>> threeSum(int[] nums) {
        return _two(nums);
    }

    private List<List<Integer>> _two(int[] nums) {

        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length == 0 || nums.length < 3) {
            return result;
        }
        sort(nums);
        List<Integer> list = null;

        Set<String> hashSet = new HashSet<>();

        String key = null;
        for (int i = 0; i < nums.length; i++) {
            int[] temp = twoSum(nums, (0 - nums[i]), i);
            if (temp == null || temp.length == 0) {
                continue;
            }
            temp[2] = nums[i];
            sort(temp);
            key = temp[0] + "-" + temp[1] + "-" + temp[2];
            if (hashSet.contains(key)) {
                continue;
            } else {
                hashSet.add(key);
                list = new ArrayList<>(3);
                list.add(temp[0]);
                list.add(temp[1]);
                list.add(temp[2]);
                result.add(list);
            }
        }
        return result;
    }

    private List<List<Integer>> _one(int[] nums) {
        sort(nums);

        List<List<Integer>> result = new ArrayList<>();

        List<Integer> list = null;
        Set<String> set = new HashSet<>();

        LOOP:
        for (int i = 0; i < nums.length - 2; i++) {
            if (nums[i] > 0) {
                break;
            }
            LOOP_TWO:
            for (int j = i + 1; j < nums.length - 1; j++) {
                if (nums[i] + nums[i + 1] > 0) {
                    break LOOP;
                }
                for (int l = j + 1; l < nums.length; l++) {
                    if (nums[i] + nums[j] + nums[l] > 0) {
                        break;
                    }
                    if (nums[i] + nums[j] + nums[l] == 0) {
                        list = new ArrayList<>();
                        list.add(nums[i]);
                        list.add(nums[j]);
                        list.add(nums[l]);
                        String key = nums[i] + "-" + nums[j] + "-" + nums[l];
                        if (set.contains(key)) {
                            break;
                        } else {
                            set.add(key);
                            result.add(list);
                        }
                    }
                }
            }
        }

        return result;
    }

    private void sort(int[] array) {
        int length = array.length;
        int i, j;
        for (i = 1; i < length; i++) {

            for (j = i - 1; j >= 0 && array[i] < array[j]; j--) {
            }

            //这里跳出内层循环，a[i]应被插入到a[j]后
            int tmp = array[i];
            for (int k = i; k > j + 1; k--) {
                array[k] = array[k - 1];
            }
            array[j + 1] = tmp;

        }
    }

    private int[] twoSum(int[] nums, int target, int index) {

        int[] result = new int[3];

        // 建立k-v ，一一对应的哈希表
        HashMap<Integer, Integer> hash = new HashMap<Integer, Integer>();
        for (int i = 0; i < nums.length; i++) {
            // if (i == index) {
            //     continue;
            // }
            if (hash.containsKey(nums[i])) {
                result[0] = nums[i];
                result[1] = target - nums[i];
                return result;
            }
            // 将数据存入 key为补数 ，value为下标
            hash.put(target - nums[i], i);
        }
        return null;
    }
}
