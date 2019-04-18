package com.hzx.sort;

import com.alibaba.fastjson.JSON;
import com.hzx.sort.bucket.BucketSort;
import com.hzx.sort.heap.HeapSort;
import com.hzx.sort.merge.MergeSort;
import com.hzx.sort.quick.QuickSort;
import com.hzx.sort.radix.RadixSort;
import com.hzx.sort.shell.ShellSort;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.hzx.sort.BaseSort.randomArray;

public class SortDemoApplication {

    private static Map<String, String> map = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        Class clazz = JobTest.class;
        JobTest jobTest = new JobTest();
        Class clazzz = jobTest.getClass();
        System.out.println(clazz==clazzz);
        System.out.println(JobTest.A);
    }

    /**
     * InheritableThreadLocal 验证
     */
    public static void test11() {
        TestClass.threadLocal.set("哈哈哈哈");

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int finalI = i;
                System.out.println("第 （" + finalI + "） 次in sub thread  -----> " + TestClass.threadLocal.get());
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {

                }
            }
        });

        t1.start();

        try {
            Thread.sleep(10);
            TestClass.threadLocal.set("nonononono");
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        TestClass.threadLocal.remove();
    }

    /**
     * wait / notify 测试
     * 为验证，当前线程仅仅wait，其他线程会不会获取到锁
     */
    public static void test10() {
        Object object = new Object();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "-------->hahahah");
                try {
                    object.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("是不是我永远都看不到了？");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "-------->hahahah");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        new Thread(() -> {
            synchronized (object) {
                System.out.println(Thread.currentThread().getName() + "-------->hahahah");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        try {
            Thread.sleep(12000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("唤醒");
        synchronized (object) {
            object.notify();
        }
    }

    /**
     * synchroized测试继承
     */
    public static void test9() {
        Main a = new Sub();
        new Thread(() -> {
            a.test();
        }).start();

        new Thread(() -> {
            a.test();
        }).start();

    }

    /**
     * 测试Thread.stop对锁的影响
     */
    public static void test8() {

        Lock lock = new ReentrantLock();

        Thread t1 = new Thread(() -> {
            lock.lock();
            try {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "这是t1");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        });

        Thread t2 = new Thread(() -> {
            lock.lock();
            try {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "这是t2");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        });

        Thread t3 = new Thread(() -> {
            lock.lock();
            try {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        System.out.println(Thread.currentThread().getName() + "这是t3");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } finally {
                lock.unlock();
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t1.stop();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t2.stop();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        t3.stop();

    }

    public static void test7() {
        Thread t = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + "开始执行了！");

            for (int i = 0; i < 900; i++) {
                try {
                    Thread.sleep(2);
                    System.out.println("lalalal");
                } catch (InterruptedException e) {
                    // e.printStackTrace();
                }
                Thread.yield();
            }
            System.out.println(Thread.currentThread().getName() + "执行完了！");
        });
        t.start();

        System.out.println("等一下");
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // t.interrupt();
        boolean flag = t.isInterrupted();
        System.out.println(flag);
        // t.stop();
    }

    public static void test6() {
        map.put(new Random().nextInt() + "1", "2");
        test6();
    }

    public static void test5() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future future = executorService.submit(() -> {
            System.out.println("线程名：" + Thread.currentThread().getName());
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // future.cancel(true);

        // try {
        //     Thread.sleep(100);
        // } catch (InterruptedException e) {
        //     e.printStackTrace();
        // }
        boolean flag = executorService.isTerminated();
        System.out.println("isTerminater?  " + flag);
        flag = executorService.isShutdown();
        System.out.println("isShutdown? " + flag);

        executorService.submit(() -> {
            System.out.println(Thread.currentThread().getName() + " 这是我第二次被使用！");
        });

        executorService.shutdown();
    }

    public static void test4() {
        final Map<String, String> map = new HashMap<>();
        map.put("1", "2");
        System.out.println(map);
    }

    /**
     * FutureTask测试
     */
    public static void test3() throws InterruptedException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future<String> future = executorService.submit(() -> {
            System.out.println("进入futureTask！");
            sort();
            return "hhhh";
        });

        for (int i = 0; i < 50; i++) {
            executorService.execute(() -> {
                sort_test();
            });
        }

        for (int i = 0; i < 100; i++) {
            boolean flag = future.cancel(true);
            System.out.println("取消结果：" + flag);
            Thread.sleep(100);
        }

        executorService.shutdown();
    }

    /**
     * MVEL 测试
     */
    public static void test2() {
        // 以单引号包裹的作为字符串，不以单引号包裹的需要外部传值
        Map<String, String> pparamter = new HashMap<>();
        pparamter.put("key", "hbc");
        Map<String, Object> paramter = new HashMap<>();
        paramter.put("abc", "hahahah");
        paramter.put("name1", "hahahh");
        paramter.put("cde", JSON.toJSONString(pparamter));
        Map resultMap = MVEL.eval("{'key1':cde.key,'key2':name1}", paramter, Map.class);
        System.out.println(resultMap);
        System.out.println(resultMap.get("key1"));
        String json = JSON.toJSONString(resultMap);
        System.out.println(json);

        //
        Map<String, Object> intMap = new HashMap<>();
        intMap.put("key", 1);
        int i = MVEL.eval("3*key + 1", intMap, Integer.class);
        // 当前情况，key 由 单引号包裹，则不应该由外部传入，则报错
        // int i = MVEL.eval("3*'key' + 1",intMap,Integer.class);
        System.out.println(i);

    }

    /**
     * 线程中断测试
     */
    public static void test() {
        ExecutorService executorService = Executors.newCachedThreadPool();

        Future<String> future = executorService.submit(() -> {
            System.out.println("已经进入当前线程！");

            // sort();
            for (int i = 0; i < 20; i++) {
                System.out.println("*****************");
                Thread.sleep(100);
            }

            String threadName = Thread.currentThread().getName();
            System.out.println("当前线程名:" + threadName);
            return threadName;
        });

        try {
            Thread.sleep(701);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        boolean flag = future.cancel(true);
        System.out.println("已经执行了取消线程的操作！flag：" + flag);

        flag = future.isCancelled();
        System.out.println("是否取消当前线程的操作！flag：" + flag);

        executorService.shutdownNow();
        flag = executorService.isTerminated();
        System.out.println("线程池终止状态：" + flag);
    }

    public static void sort() throws InterruptedException {
        System.out.println("==============Heap=============");
        int[] array = randomArray();
        long start = System.currentTimeMillis();
        HeapSort.sort(array);
        System.out.println("Heap sort cost time : " + (System.currentTimeMillis() - start));
        for (int i = 0; i < 10000; i++) {
            Thread.yield();
            // Thread.sleep(0,1);
        }

        System.out.println("==============Shell=============");
        array = randomArray();
        start = System.currentTimeMillis();
        ShellSort.sort(array);
        System.out.println("Shell sort cost time : " + (System.currentTimeMillis() - start));
        for (int i = 0; i < 10000; i++) {
            Thread.yield();
            // Thread.sleep(0,1);
        }

        System.out.println("==============Quick=============");
        array = randomArray();
        start = System.currentTimeMillis();
        QuickSort.sort(array, 0, array.length - 1);
        System.out.println("Quick sort cost time : " + (System.currentTimeMillis() - start));
        for (int i = 0; i < 10000; i++) {
            Thread.yield();
            // Thread.sleep(0,1);
        }

        System.out.println("==============Merge=============");
        array = randomArray();
        start = System.currentTimeMillis();
        MergeSort.sort(array, 0, array.length - 1);
        System.out.println("Merge sort cost time : " + (System.currentTimeMillis() - start));
        for (int i = 0; i < 10000; i++) {
            Thread.yield();
            // Thread.sleep(0,1);
        }

        System.out.println("==============Bucket=============");
        array = randomArray();
        start = System.currentTimeMillis();
        BucketSort.sort(array);
        System.out.println("Bucket sort cost time : " + (System.currentTimeMillis() - start));
        for (int i = 0; i < 10000; i++) {
            Thread.yield();
            // Thread.sleep(0,1);
        }

        System.out.println("==============Radix=============");
        array = randomArray();
        start = System.currentTimeMillis();
        RadixSort.lsdSort(array);
        System.out.println("Radix sort cost time : " + (System.currentTimeMillis() - start));
        for (int i = 0; i < 10000; i++) {
            Thread.yield();
            // Thread.sleep(0,1);
        }
    }

    public static void sort_test() {
        int[] array = randomArray();
        HeapSort.sort(array);
        Thread.yield();

        array = randomArray();
        ShellSort.sort(array);
        Thread.yield();

        array = randomArray();
        QuickSort.sort(array, 0, array.length - 1);
        Thread.yield();

        array = randomArray();
        MergeSort.sort(array, 0, array.length - 1);
        Thread.yield();

        array = randomArray();
        BucketSort.sort(array);
        Thread.yield();

        array = randomArray();
        RadixSort.lsdSort(array);
        Thread.yield();
    }

    static class Main {

        static int k;

        synchronized void test() {
            for (int i = 0; i < 1000; i++) {
                k++;
                System.out.println(Thread.currentThread().getName() + "Main k:" + k);
            }
        }
    }

    static class Sub extends Main {
        @Override
        void test() {
            for (int i = 0; i < 1000; i++) {
                k++;
                System.out.println(Thread.currentThread().getName() + "Sub k:" + k);
            }
            super.test();
        }
    }

}

