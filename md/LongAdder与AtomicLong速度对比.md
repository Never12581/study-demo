### LongAdder与AtomicLong 在高并发下效率对比

- 结果

```
========================================
条件>>>>>>线程数:10, 单线程操作计数10000
结果>>>>>>LongAdder方式增加计数100000次,共计耗时:132
计算结果为：100000
条件>>>>>>线程数:10, 单线程操作计数10000
结果>>>>>>AtomicLong方式增加计数100000次,共计耗时:5
计算结果为：100000
========================================
条件>>>>>>线程数:10, 单线程操作计数10000
结果>>>>>>LongAdder方式增加计数100000次,共计耗时:7
计算结果为：100000
条件>>>>>>线程数:10, 单线程操作计数10000
结果>>>>>>AtomicLong方式增加计数100000次,共计耗时:7
计算结果为：100000
========================================
条件>>>>>>线程数:20, 单线程操作计数200000
结果>>>>>>LongAdder方式增加计数4000000次,共计耗时:14
计算结果为：4000000
条件>>>>>>线程数:20, 单线程操作计数200000
结果>>>>>>AtomicLong方式增加计数4000000次,共计耗时:118
计算结果为：4000000
========================================
条件>>>>>>线程数:50, 单线程操作计数500000
结果>>>>>>LongAdder方式增加计数25000000次,共计耗时:56
计算结果为：25000000
条件>>>>>>线程数:50, 单线程操作计数500000
结果>>>>>>AtomicLong方式增加计数25000000次,共计耗时:652
计算结果为：25000000
```

- 源码

```java
public class LongAddrAndAtomicLongTest {
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
        }
    }

    static long testAtomicLong(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        AtomicLong atomicLong = new AtomicLong();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for(int k = 0 ; k < times ; k++) {
                    atomicLong.addAndGet(1L);
                }
                countDownLatch.countDown();
            });
        }
        countDownLatch.await();
        executor.shutdown();
        return atomicLong.get();
    }

    static long testLongAdder(final int threadCount, final int times) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);
        LongAdder longAdder = new LongAdder();
        ExecutorService executor = Executors.newCachedThreadPool();
        for (int i = 0; i < threadCount; i++) {
            executor.submit(()->{
                for(int k = 0 ; k < times ; k++) {
                    longAdder.add(1L);
                }
                countDownLatch.countDown();
            });
        }

        countDownLatch.await();
        executor.shutdown();
        return longAdder.sum();
    }
}
```

