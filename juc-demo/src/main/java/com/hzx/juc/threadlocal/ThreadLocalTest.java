package com.hzx.juc.threadlocal;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:36 2019/8/28
 */
public class ThreadLocalTest {

    private final static ThreadLocal<TestUser> USER_THREAD_LOCAL = new ThreadLocal<>();

    public static void main(String[] args) throws Exception {
        test3();
    }



    public static void test5() {
        ThreadLocal<Integer> t1 = new ThreadLocal<>();
        t1.set(333);

        System.gc();
        System.runFinalization();

        System.out.println(t1.get());
    }

    public static void test1() {
        System.out.println(Thread.currentThread().getName());
        String s = "aaa";
        new Thread(() -> {
            TConstants.set(s);
            System.out.println(Thread.currentThread().getName() + "---------" + TConstants.get());
            TConstants.clear();
        }).start();
    }

    /**
     * 证明 ThreadLocal不清理，上个线程保存的东西，能在下个线程拿到
     * @throws InterruptedException
     */
    public static void test2() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(1);

        List<String> list = new ArrayList<>(1024);

        for (int i = 0; i < 1024; i++) {
            list.add("a" + i);
        }

        es.execute(() -> {
            String s = "abc";
            TConstants.set(s);
            print("---------" + TConstants.get());

        });

        Thread.sleep(1000L);

        es.execute(() -> {
            System.out.println(Thread.currentThread().getName() + " 在同一个线程另一个任务里 ============ " + TConstants.get());
        });
        es.shutdown();
    }

    /**
     * 证明ThreadLocal存储的内存是深度拷贝的。
     */
    public static void test3() throws NoSuchFieldException, IllegalAccessException {
        Integer a = 333;
        IntegerConstants.set(a);

        Class intClass = a.getClass();
        Field valueField = intClass.getDeclaredField("value");
        valueField.setAccessible(true);
        valueField.set(a,444);

        aaa();
        System.out.println("main " + a);

        // TestUser testUser = new TestUser("root","123");
        // USER_THREAD_LOCAL.set(testUser);
        // testUser.setUserPwd("456");
        //
        // System.out.println("int main : " + testUser.hashCode());
        //
        // bbb();

    }

    private static void bbb() {
        System.out.println("in method bbb "+USER_THREAD_LOCAL.get().getUserName()+"-"+USER_THREAD_LOCAL.get().getUserPwd());
        System.out.println("in method bbb "+USER_THREAD_LOCAL.get().hashCode());
    }

    public static void test4() {
        ThreadLocal<Map<Integer, String>> threadLocal1 = new ThreadLocal<>();
        Map<Integer, String> map1 = new HashMap<>(1);
        map1.put(1, "我是第1个ThreadLocal数据！");
        threadLocal1.set(map1);
        threadLocal1 = null;
        System.gc(); //强制执行GC
        System.runFinalization();

        ThreadLocal<Map<Integer, String>> threadLocal2 = new ThreadLocal<>();
        Map<Integer, String> map2 = new HashMap<>(1);
        map2.put(2, "我是第2个ThreadLocal数据！");
        threadLocal2.set(map2);
        threadLocal2 = null;
        System.gc(); //强制执行GC
        System.runFinalization();

        ThreadLocal<Map<Integer, String>> threadLocal3 = new ThreadLocal<>();
        Map<Integer, String> map3 = new HashMap<>(1);
        map3.put(3, "我是第3个ThreadLocal数据！");
        threadLocal3.set(map3);

        ThreadLocal<Map<Integer, String>> threadLocal4 = new ThreadLocal<>();
        Map<Integer, String> map4 = new HashMap<>(1);
        map4.put(4, "我是第4个ThreadLocal数据！");
        threadLocal4.set(map4);
        System.out.println("-------" + threadLocal3.get());
    }

    private static void aaa() {
        System.out.println("int aaa " + IntegerConstants.get());
    }

    public static void print(Object object) {
        System.out.println(Thread.currentThread().getName() + "=========" + object.toString());
    }

}
