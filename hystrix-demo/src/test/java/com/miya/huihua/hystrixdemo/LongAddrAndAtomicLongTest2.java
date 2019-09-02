package com.miya.huihua.hystrixdemo;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

public class LongAddrAndAtomicLongTest2 {
    public static void main(String[] args) {
        System.out.println("========================================");
        testAtomicLongVSLongAdder(10, 10000);
        System.out.println("========================================");
        testAtomicLongVSLongAdder(10, 10000);
        System.out.println("========================================");
        testAtomicLongVSLongAdder(20, 200000);
        System.out.println("========================================");
        testAtomicLongVSLongAdder(50, 500000);
    }

    static void testAtomicLongVSLongAdder(final int threadCount, final int times) {
        try {
            long start = System.currentTimeMillis();
            long longAddr = testLongAdder(threadCount, times);
            long end = System.currentTimeMillis() - start;
            System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>LongAdder方式增加计数" + (threadCount * times) + "次,共计耗时:" + end);
            System.out.println("计算结果为："+longAddr);

            long start2 = System.currentTimeMillis();
            long atomicLong = testAtomicLong(threadCount, times);
            long end2 = System.currentTimeMillis() - start2;
            System.out.println("条件>>>>>>线程数:" + threadCount + ", 单线程操作计数" + times);
            System.out.println("结果>>>>>>AtomicLong方式增加计数" + (threadCount * times) + "次,共计耗时:" + end2);
            System.out.println("计算结果为："+atomicLong);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }
    }

    static long testAtomicLong(final int threadCount, final int times)
            throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount+1,Thread.currentThread());
        AtomicLong atomicLong = new AtomicLong();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for(int k = 0 ; k < times ; k++) {
                    atomicLong.addAndGet(1L);
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }
        cyclicBarrier.await();
        executor.shutdown();

        return atomicLong.get();
    }

    static long testLongAdder(final int threadCount, final int times)
            throws InterruptedException, BrokenBarrierException {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(threadCount+1);
        LongAdder longAdder = new LongAdder();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(()->{
                for(int k = 0 ; k < times ; k++) {
                    longAdder.add(1L);
                }
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        cyclicBarrier.await();
        executor.shutdown();
        return longAdder.sum();
    }
}
