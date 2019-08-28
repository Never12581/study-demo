## ThreadLocal解析

### 概念

ThreadLocal 可以防止共享资源产生冲突的。线程本地存储是一种自动化机制，可以为使用相同变量的每个不同线程创建不同的存储。ThreadLocal对象通常当作静态域空间。在创建ThreadLocal时，你只能通过get()和set()方法来访问该对象的内容，其中，get()方法将返回与线程相关联的对象的副本，而set()会将参数插入到为其献策很难过存储的对象中，并返回存储中原有到对象。

> 简言之，ThreadLocal是对Thread对象的ThreadLocal.ThreadLocalMap的简单封装，因为ThreadLocal.ThreadLocalMap只属于单个存活线程，所以存放在ThreadLocal.ThreadLocalMap中的数据总是安全的。

### 用法

```java
public class TConstants {
    private final static ThreadLocal<String> t = new ThreadLocal<>();
    public static void set(String s) {
        t.set(s);
    }
    public static String get(){
        return t.get();
    }
    public static void clear(){
        t.remove();
    }
}

public class ThreadLocalTest {
    public static void main(String[] args) {
        System.out.println(Thread.currentThread().getName());
        String s = "aaa";
        new Thread(() -> {
            TConstants.set(s);
            System.out.println(Thread.currentThread().getName() + "---------" + TConstants.get());
          	TConstants.clear();
        }).start();
    }
}

```

- 在使用完以后一定要清理，不然会造成内存溢出。理由后述

### 源码分析

```java
public class ThreadLocal<T> {
  
  public void set(T value) {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null)
            map.set(this, value);
        else
            createMap(t, value);
    }
  
  public T get() {
        Thread t = Thread.currentThread();
        ThreadLocalMap map = getMap(t);
        if (map != null) {
            ThreadLocalMap.Entry e = map.getEntry(this);
            if (e != null) {
                T result = (T)e.value;
                return result;
            }
        }
        return setInitialValue();
    }
  
  public void remove() {
         ThreadLocalMap m = getMap(Thread.currentThread());
         if (m != null)
             m.remove(this);
     }
  
}

static class ThreadLocalMap {

        /**
         * The entries in this hash map extend WeakReference, using
         * its main ref field as the key (which is always a
         * ThreadLocal object).  Note that null keys (i.e. entry.get()
         * == null) mean that the key is no longer referenced, so the
         * entry can be expunged from table.  Such entries are referred to
         * as "stale entries" in the code that follows.
         */
        static class Entry extends WeakReference<ThreadLocal<?>> {
            /** The value associated with this ThreadLocal. */
            Object value;

            Entry(ThreadLocal<?> k, Object v) {
                super(k);
                value = v;
            }
        }
}
```

其实这样看，是不会出现问题的！因为核心的Entry是一个虚引用，会被回收。

但是问题是，现在多数都是多线程的服务，如果你不清理当前线程使用的ThreadLocalMap，那么在一个线程，也能得到它，没错，恐怖吧。

