package com.hzx.juc.lock;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @Author: bocai.huang
 * @Descripition: 测试读写锁
 * @Date: Create in 20:44 2019/4/14
 */
public class TestReentrantReadWriteLock {

    private static ReadWriteLock lock = new ReentrantReadWriteLock();

    public static void main(String[] args) throws InterruptedException {
        // ================读锁测试================
        // new Thread(()->{
        //     readTest();
        // }).start();
        //
        // new Thread(()->{
        //     readTest();
        // }).start();


        // ================写锁测试================
        // new Thread(()->{
        //     writeTest();
        // }).start();
        //
        // new Thread(()->{
        //     writeTest();
        // }).start();

        // ==============先写后读==============
        new Thread(()->{
            writeTest();
        }).start();

        Thread.sleep(10);

        new Thread(()->{
            readTest();
        }).start();

        // ==============先读后写==============
        // new Thread(()->{
        //     readTest();
        // }).start();
        //
        // Thread.sleep(10);
        //
        // new Thread(()->{
        //     writeTest();
        // }).start();

    }

    public static void readTest() {
        try {
            lock.readLock().lock();
            System.out.println("获取读锁 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.readLock().unlock();
        }
    }

    public static void writeTest(){
        try {
            lock.writeLock().lock();
            System.out.println("获取写锁 " + Thread.currentThread().getName() + " " + System.currentTimeMillis());
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.writeLock().unlock();
        }
    }

}
