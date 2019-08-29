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

其实这样看，是不会出现问题的！因为核心的Entry是一个虚引用，会被回收。ThreadLocal在ThreadLocalMap中是以一个弱引用身份被Entry中的Key引用的，因此如果ThreadLocal没有外部强引用来引用它，那么ThreadLocal会在下次JVM垃圾收集时被回收。这个时候就会出现Entry中Key已经被回收，出现一个null Key的情况，外部读取ThreadLocalMap中的元素是无法通过null Key来找到Value的。因此如果当前线程的生命周期很长，一直存在，那么其内部的ThreadLocalMap对象也一直生存下来，这些null key就存在一条强引用链的关系一直存在：Thread --> ThreadLocalMap-->Entry-->Value，这条强引用链会导致Entry不会回收，Value也不会回收，但Entry中的Key却已经被回收的情况，造成内存泄漏。

但是JVM团队已经考虑到这样的情况，并做了一些措施来保证ThreadLocal尽量不会内存泄漏：在ThreadLocal的get()、set()、remove()方法调用的时候会清除掉线程ThreadLocalMap中所有Entry中Key为null的Value，并将整个Entry设置为null，利于下次内存回收。

还有问题是，现在多数都是多线程的服务，如果你不清理当前线程使用的ThreadLocalMap，那么在一个线程，也能得到它，没错，恐怖吧。



### 总结

- 在当前线程池的背景下，如果使用ThreadLocal不进行清理，那么当前线程在下一个任务中能得到上个任务存入ThreadLocal中的值
- 使用static的ThreadLocal，延长了ThreadLocal的生命周期，可能导致的内存泄漏。分配使用了ThreadLocal又不再调用get()、set()、remove()方法，那么就会导致内存泄漏。

### 问题

1. ThreadLocal.ThreadLocalMap.Entry，是一个虚引用，会在下次GC时被回收，这样线程就无法正常的get到存入ThreadLocal中的值，造成线程问题怎么办？

   答：在实际情况中使用ThreadLocal，一般都是以上述TConstants类的形式来使用ThreadLocal的。而弱引用对象在下次GC被回收呢，是指当前对象有且仅有弱引用时，才会被回收。一般使用TConstants形式使用，故ThreadLocal对象出了在Entry中的弱引用外，还存在一个强引用，故不会被回收。

   所以，在我们一般使用的时候，这个key并不会被♻️！如果我们定义了足够多的ThreadLocal，或者ThreadLocal中存储的对象是特别大的，且不手动清理，就会造成内存泄漏，然后内存溢出！

   所以，一定要回收啊，问题会有很多的！！！