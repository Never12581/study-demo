package com.hzx.nowcoder;

import java.util.Scanner;

/**
 * 子网掩码分类统计
 * <a href="https://www.nowcoder.com/practice/de538edd6f7e4bc3a5689723a7435682?tpId=37&tqId=21241&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking&tPage=1"></a>
 * 40% 通过率，感觉很不应该
 */
public class TypeSort {

    /**
     * 请解析IP地址和对应的掩码，进行分类识别。要求按照A/B/C/D/E类地址归类，不合法的地址和掩码单独归类。
     * <p>
     * 所有的IP地址划分为 A,B,C,D,E五类
     * A类地址1.0.0.0~126.255.255.255;
     * 00000001.00000000.00000000.00000000 ~ 01111110.11111111.11111111.11111111
     * B类地址128.0.0.0~191.255.255.255;
     * 10000000.00000000.00000000.00000000 ~ 10111111.11111111.11111111.11111111
     * C类地址192.0.0.0~223.255.255.255;
     * 11000000.00000000.00000000.00000000 ~ 11011111.11111111.11111111.11111111
     * D类地址224.0.0.0~239.255.255.255；
     * 11100000.00000000.00000000.00000000 ~ 11101111.11111111.11111111.11111111
     * E类地址240.0.0.0~255.255.255.255
     * 11110000.00000000.00000000.00000000 ~ 11111111.11111111.11111111.11111111
     * <p>
     * 私网IP范围是：
     * 10.0.0.0～10.255.255.255
     * 00001010.00000000.00000000.00000000 ~ 00001010.11111111.11111111.11111111
     * 172.16.0.0～172.31.255.255
     * 10101100.00010000.00000000.00000000 ~ 10101100.00011111.11111111.11111111
     * 192.168.0.0～192.168.255.255
     * 11000000.10101000.00000000.00000000 ~ 11000000.10101000.11111111.11111111
     * <p>
     * 子网掩码为二进制下前面是连续的1，然后全是0。（例如：255.255.255.32就是一个非法的掩码）
     * 注意二进制下全是1或者全是0均为非法
     * <p>
     * 注意：
     * 1. 类似于【0.*.*.*】的IP地址不属于上述输入的任意一类，也不属于不合法ip地址，计数时可以忽略
     * 2. 私有IP地址和A,B,C,D,E类地址是不冲突的
     * <p>
     * 输入描述:
     * 多行字符串。每行一个IP地址和掩码，用~隔开。
     * <p>
     * 输出描述:
     * 统计A、B、C、D、E、错误IP地址或错误掩码、私有IP的个数，之间以空格隔开。
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int a = 0;
        int b = 0;
        int c = 0;
        int d = 0;
        int e = 0;
        int err = 0;
        int pri = 0;

        while (sc.hasNextLine()) {
            String source = sc.nextLine();
            String[] ss = source.split("~");
            String ip = ss[0];
            String subNetMask = ss[1];
            IpParagraph ipParagraph = new IpParagraph();
            boolean ipFlag = ipParagraph.init(ip);
            SubMarkParagraph subnet = new SubMarkParagraph();
            boolean subNetMaskflag = subnet.init(subNetMask);
            // 如果出现任何异常，则证明ip非法
            if (!ipFlag || !subNetMaskflag) {
                err += 1;
                continue;
            }

            // 判断是否忽略
            if (ipParagraph.isIngore()) {
                continue;
            }

            if (ipParagraph.isPri()) {
                pri++;
            }

            if (ipParagraph.isA()) {
                a++;
                continue;
            }

            if (ipParagraph.isB()) {
                b++;
                continue;
            }

            if (ipParagraph.isC()) {
                c++;
                continue;
            }

            if (ipParagraph.isD()) {
                d++;
                continue;
            }

            if (ipParagraph.isE()) {
                e++;
                continue;
            }

        }
        sc.close();
        System.out.println(a + " " + b + " " + c + " " + d + " " + e + " " + err + " " + pri);

    }

    public static abstract class Paragraph {

        protected String ip;

        protected int first;
        protected int second;
        protected int third;
        protected int forth;

        protected boolean init(String s) {
            ip = s;
            String[] ss = s.split("\\.");
            try {
                first = Integer.parseInt(ss[0]);
                second = Integer.parseInt(ss[1]);
                third = Integer.parseInt(ss[2]);
                forth = Integer.parseInt(ss[3]);
                return check();
            } catch (NumberFormatException e) {
                return false;
            }
        }

        protected abstract boolean check();
    }

    public static class IpParagraph extends Paragraph {
        protected boolean check() {
            if (first >= 255 || first < 1 || first == 127) {
                return false;
            }
            if (second > 255 || second < 0) {
                return false;
            }
            if (third > 255 || third < 0) {
                return false;
            }
            if (forth > 255 || forth < 0) {
                return false;
            }
            if (first == 0 && second == 0 && third == 0 && forth == 0) {
                return false;
            }
            if (first == 255 && second == 255 && third == 255 && forth == 255) {
                return false;
            }
            return true;
        }

        public boolean isIngore() {
            if (first == 0 || first == 127) {
                return true;
            }
            return false;
        }

        public boolean isA() {
            if (first >= 1  && first <= 126) {
                return true;
            }
            return false;
        }

        public boolean isB() {
            if (first >= 128 && first <= 191) {
                return true;
            }
            return false;
        }

        public boolean isC() {
            if (first >= 192 && first <= 223) {
                return true;
            }
            return false;
        }

        public boolean isD() {
            if (first >= 224 && first <= 239) {
                return true;
            }
            return false;
        }

        public boolean isE() {
            if (first >= 240 && first <= 255) {
                return true;
            }
            return false;
        }

        public boolean isPri() {
            if (first == 10) {
                return true;
            }
            if (first == 172 && second >= 16 && second <= 31) {
                return true;
            }
            if (first == 192 && second == 168) {
                return true;
            }
            return false;
        }
    }

    public static class SubMarkParagraph extends Paragraph {

        protected boolean check() {
            if ("0.0.0.0".equals(ip) || "255.255.255.255".equals(ip)) {
                return false;
            }
            if (first < 1 || first > 255 || second < 0 || second > 255
                || third < 0 || third > 255 || forth < 0 || forth >255) {
                return false;
            }
            StringBuilder binaryIp = new StringBuilder();
            binaryIp.append(Integer.toBinaryString(first)).append(".")
                    .append(Integer.toBinaryString(second)).append(".")
                    .append(Integer.toBinaryString(third)).append(".")
                    .append(Integer.toBinaryString(forth));
            String binary = binaryIp.toString();
            int firstZero = binary.indexOf("0");
            int lastOne = binary.lastIndexOf("1");
            return firstZero > lastOne;
        }
    }

}
