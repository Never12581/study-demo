package com.hzx.nowcoder;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class PasswordVerifier {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        A:
        while (sc.hasNextLine()) {
            String line = sc.nextLine();

            if (line.length() <= 8) {
                System.out.println("NG");
                continue;
            }

            char[] ch = line.toCharArray();
            int numCount = 0;
            int lowCount = 0;
            int highCount = 0;
            int other = 0;
            for (int i = 0; i < line.length(); i++) {
                if (ch[i] >= '0' && ch[i] <= '9') {
                    if (numCount == 0) {
                        numCount++;
                    }
                } else if (ch[i] >= 'a' && ch[i] <= 'z') {
                    if (lowCount == 0) {
                        lowCount++;
                    }
                } else if (ch[i] >= 'A' && ch[i] <= 'Z') {
                    if (highCount == 0) {
                        highCount++;
                    }
                } else {
                    if (other == 0) {
                        other++;
                    }
                }
                if (i > 4) {
                    if ((numCount + lowCount + highCount + other) >= 3) {
                        break;
                    }
                }
            }
            if ((numCount + lowCount + highCount + other) < 3) {
                System.out.println("NG");
                continue;
            }

            Set<String> set = new HashSet<>();
            String sub;
            for (int i = 3; i < line.length(); i++) {
                sub = line.substring(i - 3, i);
                if (set.contains(sub)) {
                    System.out.println("NG");
                    continue A;
                } else {
                    set.add(sub);
                }
            }

            System.out.println("OK");

        }
    }

}
