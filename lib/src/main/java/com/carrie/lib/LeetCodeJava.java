package com.carrie.lib;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class LeetCodeJava {

    public static void main(String[] args) {
        int[] nums1 = {1, 2};
        int[] nums2 = {1, 1};
        if (nums2.length > nums1.length) {
            int[] result = intersect(nums2, nums1);
            System.out.println("result: " + Arrays.toString(result));
        } else {
            int[] result = intersect(nums1, nums2);
            System.out.println("result: " + Arrays.toString(result));
        }

    }

    public static int[] intersect(int[] nums1, int[] nums2) {
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int num : nums1) {
            if (map.containsKey(num)) {
                map.replace(num, map.get(num) + 1);
            } else {
                map.put(num, 1);
            }
        }

        HashMap<Integer, Integer> map1 = new HashMap<>();
        for (int i = 0; i < nums2.length; i++) {
            if (map.containsKey(nums2[i])  ) {
                map1.put(i, nums2[i]);
                map.replace(nums2[i], map.get(nums2[i]-1));
            }
        }

        // 将 HashMap 的值集转换为 int[] 数组
        return map1.values().stream().mapToInt(Integer::intValue).toArray();
    }

    public static int singleNumber(int[] nums) {
        int single = 0;
        for (int num : nums) {
            single ^= num;
        }
        return single;
    }


    public static boolean containsDuplicate(int[] nums) {
        HashSet<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (!set.add(num)) {
                return true;
            }
        }
        return false;
    }
}