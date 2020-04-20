package com.hzx.nowcoder;

import java.util.Scanner;

/**
 * 坐标移动
 * <a href="https://www.nowcoder.com/practice/119bcca3befb405fbe58abe9c532eb29?tpId=37&tqId=21240&rp=0&ru=%2Fta%2Fhuawei&qru=%2Fta%2Fhuawei%2Fquestion-ranking&tPage=1"></a>
 * 擦，这里的问题竟然是，没有while (sc.haxNextLine()) ， 题目里也没说啊
 */
public class CoordinateMovement {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String commands = sc.nextLine();
            String[] command = commands.split(";");
            Point point = new Point();
            for (String com : command) {
                point.execute(com);
            }
            System.out.println(point.x + "," + point.y);
        }
        sc.close();
    }

    public static class Point {
        private int x;
        private int y;

        public Point() {
            x = 0;
            y = 0;
        }

        public void execute(String command) {
            if (command.length() > 3 || command.length() < 2) {
                return;
            }

            String direction = command.substring(0, 1);
            if ("A".equals(direction) || "S".equals(direction) || "D".equals(direction) || "W".equals(direction)) {
                String s = command.substring(1, command.length());
                int size = 0;
                try {
                    size = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return;
                }
                move(direction, size);
            }
        }

        private void move(String direction, int size) {
            if ("A".equals(direction)) {
                x -= size;
            } else if ("S".equals(direction)) {
                y -= size;
            } else if ("D".equals(direction)) {
                x += size;
            } else {
                y += size;
            }
        }

    }

}
