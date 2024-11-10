package com.carrie.practicecustomview;

public class Test {
}

// Main class

class GFG {
    // main driver method
    public static void main(String args[]) {
        // x is stored using 32 bit 2's complement form.
        // 1的二进制是00000001,以32位储存为00000000 00000000 00000000 00000001
        // 负数取反：11111111 11111111 11111111 11111110
        // 补码加1： 11111111 11111111 11111111 11111111

        // Binary representation of -1 is all 1s
        int x = -1;

        // 向右移动29位，表示从右到左的29位数都被去掉，最左边的3位111 移到了右边，前面以0补齐
        // 32-29=3 , 表示只留下了最左边3位数，再以0在前面补齐

        // The value of 'x>>>29' is 00000000 00000000 00000000 00000111
        System.out.println(x >>> 29);  // 7

        // The value of 'x>>>30' is 00...00000011
        System.out.println(x >>> 30); // 3

        // The value of 'x>>>31' is 00...00000001
        System.out.println(x >>> 31); // 1

    }
}

