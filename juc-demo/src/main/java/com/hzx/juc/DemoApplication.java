package com.hzx.juc;

// @SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        // SpringApplication.run(DemoApplication.class, args);

        System.out.println(test1());
    }

    public static int test1() {
        int b = 0;
        try {
            System.out.println("try");
            return b;
        } finally {
            System.out.println("finally");
            // b = b + 100;
            b++;
            ++b;
        }
    }

}
