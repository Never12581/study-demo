package com.hzx.nowcoder;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class SimpleErrorLog {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        String line;
        Map<String, Integer> map = new LinkedHashMap<>();
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] file = line.split(" ");
            String fileName = file[0].substring(file[0].lastIndexOf("\\")+1);
            if (fileName.length() > 16) {
                fileName = fileName.substring(fileName.length() -16);
            }
            String fileInfo = fileName +" " + file[1];
            if (map.containsKey(fileInfo)) {
                Integer i = map.get(fileInfo);
                map.put(fileInfo, i + 1);
            } else {
                map.put(fileInfo, 1);
            }
        }

        int count = 0;
        for (String key : map.keySet()) {
            count++;
            if (count > (map.size() - 8)) {
                System.out.println(key + " " + map.get(key));
            }
        }
    }

}
