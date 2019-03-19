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
import java.util.concurrent.FutureTask;

import static com.hzx.sort.BaseSort.randomArray;

public class SortDemoApplication {

    private static Map<String,String> map = new HashMap<>();

    public static void main(String[] args) {
        test7();
    }

    public static void test7(){
        Thread t = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+"开始执行了！");

            for(int i = 0 ; i < 9000000 ; i++) {
                try {
                    Thread.sleep(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Thread.yield();
            }
            System.out.println(Thread.currentThread().getName()+"执行完了！");
        });
        t.start();

        System.out.println("等一下");

        // t.interrupt();
        t.stop();
    }

    public static void test6(){
        map.put(new Random().nextInt()+"1","2");
        test6();
    }

    public static void test5() {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        Future future = executorService.submit(() -> {
            System.out.println("线程名："+Thread.currentThread().getName());
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


        executorService.submit(()->{
            System.out.println(Thread.currentThread().getName()+" 这是我第二次被使用！");
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
    public static void test3() {
        FutureTask<String> futureTask = new FutureTask<String>(() -> {
            System.out.println("进入futureTask！");
            sort();
            return "hhhh";
        });
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.submit(futureTask);
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        boolean flag = futureTask.cancel(true);
        System.out.println("取消结果：" + flag);

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

        // String threadName = null;
        // try {
        //     threadName = future.get();
        // } catch (InterruptedException e) {
        //     System.out.println(1);
        // } catch (ExecutionException e) {
        //     System.out.println(2);
        // }
        // System.out.println(threadName);

        executorService.shutdownNow();
        flag = executorService.isTerminated();
        System.out.println("线程池终止状态：" + flag);
    }

    public static void sort() {
        System.out.println("==============Heap=============");
        int[] array = randomArray();
        long start = System.currentTimeMillis();
        HeapSort.sort(array);
        System.out.println("Heap sort cost time : " + (System.currentTimeMillis() - start));

        System.out.println("==============Shell=============");
        array = randomArray();
        start = System.currentTimeMillis();
        ShellSort.sort(array);
        System.out.println("Shell sort cost time : " + (System.currentTimeMillis() - start));

        System.out.println("==============Quick=============");
        array = randomArray();
        start = System.currentTimeMillis();
        QuickSort.sort(array, 0, array.length - 1);
        System.out.println("Quick sort cost time : " + (System.currentTimeMillis() - start));

        System.out.println("==============Merge=============");
        array = randomArray();
        start = System.currentTimeMillis();
        MergeSort.sort(array, 0, array.length - 1);
        System.out.println("Merge sort cost time : " + (System.currentTimeMillis() - start));

        System.out.println("==============Bucket=============");
        array = randomArray();
        start = System.currentTimeMillis();
        BucketSort.sort(array);
        System.out.println("Bucket sort cost time : " + (System.currentTimeMillis() - start));

        System.out.println("==============Radix=============");
        array = randomArray();
        start = System.currentTimeMillis();
        RadixSort.lsdSort(array);
        System.out.println("Radix sort cost time : " + (System.currentTimeMillis() - start));
    }

    private static class A {
        private String name;

        public A(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public A setName(String name) {
            this.name = name;
            return this;
        }
    }

}

