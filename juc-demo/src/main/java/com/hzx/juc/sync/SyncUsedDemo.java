package com.hzx.juc.sync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bocai.huang
 * @Descripition: 若要验证，则A类与B类分开测试
 * @Date: Create in 10:33 2019/3/1
 */
public class SyncUsedDemo {

    private static final Logger logger = LoggerFactory.getLogger(SyncUsedDemo.class);

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();

        // sync在方法或者代码块上的使用 -----> synchronized 在方法上修饰，或者在方法内代码块上修饰，锁定的是当前的对象
        A a = new A();
        executorService.execute(() -> {
            a.test();
        });
        executorService.execute(() -> {
            a.test1();
        });
        executorService.execute(() -> {
            a.test2();
        });

        // sync在静态方法或者类上的使用 -----> synchronized 在静态方法上或者在以 synchronized(A.class) 形势时，锁定当前的类对象 对象调用不受影响
        B b = new B();
        executorService.execute(() -> {
            B.test();
        });
        executorService.execute(() -> {
            b.test1();
        });
        executorService.execute(() -> {
            b.test2();
        });

        executorService.shutdown();

    }

    static class B {

        public static synchronized void test() {
            for (int i = 0; i < 10; i++) {
                logger.info(" B readTest : {}", i);
            }
        }

        public void test1() {
            synchronized (B.class) {
                for (int i = 0; i < 10; i++) {
                    logger.info(" B test1 : {}", i);
                }
            }
        }

        public void test2() {
            for (int i = 0; i < 10; i++) {
                logger.info(" B test2 : {}", i);
            }
        }

    }

    static class A {
        public synchronized void test() {
            for (int i = 0; i < 10; i++) {
                logger.info(" A readTest : {}", i);
            }
        }

        public void test1() {
            synchronized (this) {
                for (int i = 0; i < 10; i++) {
                    logger.info(" A test1 : {}", i);
                }
            }
        }

        public void test2() {
            for (int i = 0; i < 10; i++) {
                logger.info(" A test2 : {}", i);
            }
        }

    }
}
