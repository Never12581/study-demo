package com.hzx.juc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 15:36 2019/3/4
 */
public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");

        ExecutorService executor = Executors.newCachedThreadPool();

        List<Date> dates = new ArrayList<>(10);

        for (int i = 0; i < 10; i++) {
            dates.add(new Date());
            Thread.sleep(1000);
        }
        System.out.println("=============原数据");
        for(Date date : dates) {
            System.out.println(date);
        }
        System.out.println("===============格式化后数据");
        for (Date date : dates) {
            executor.submit(() -> {
                // dates.add(simpleDateFormat.parse(s));
                String s = simpleDateFormat.format(date);
                logger.info(s);
            });
        }

        executor.shutdown();
    }

    static class A {
        private Date date;

        public Date getDate() {
            return date;
        }

        public A setDate(Date date) {
            this.date = date;
            return this;
        }
    }

}
