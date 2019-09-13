
> 本文旨在AQS的简单理解。如有不足指出，请联系**azaizai@icloud.com**，请多多指教！
> 待续

---
## 概述
AQS（AbstractQueuedSynchronizer）。什么是AQS？从名字看应该是一个抽象的排队同步器。当然，这篇文章并不会这么肤浅，
简单来说，AQS是一个抽象锁，是我们实现其他锁结构的基础。

AQS提供一个框架，用于实现依赖先进先出（FIFO）等待队列的阻塞锁和相关同步器（信号量、事件等）。此类被设计为大多数类型的同步器的有用基础，
这些同步器依赖单个原子（state）来表示状态。子类必须定义更改此状态的受保护方法，以及定义此状态对于正在获取或释放的对象意味着什么。
考虑到这些，这个类中的其他方法执行所有排队和阻塞机制。子类可以维护其他状态字段，但只有使用方法getState、setState和compareAndSetState对原子更新的state进行同步跟踪。

子类(Node)定义为用于实现其封闭类的同步属性的非公共内部帮助器类。此类支持两个模式：SHARED和EXCLUSIVE（共享和独占）。
以独占模式获取锁时，其他线程无法成功；共享模式获取锁时，其他线程可能获取成功。
在共享模式获取到锁到时候，还必须确定之后到线程（如果存在）能否获取到锁。

通常实现AQS到锁只支持独占，或者只支持共享模式，但是两种模式也可以共同生效如 ReadWriteLock 。

---

## 制锁需要实现的方法 （获取单个信号量）

- protected boolean tryAcquire(int arg) 
> 以独占模式获取锁，成功为true，失败为false。线程调用此方法获取锁。如果返回值为false，则将线程排队（如果线程尚未排队）。这（当前方法与排队方法）可以实现Lock接口的tryLock()方法。
> 默认实现 抛出 UnsupportedOperationException 异常。

- protected boolean tryRelease(int arg)
> 释放独占资源。执行释放锁，始终调用该方法。


- protected int tryAcquireShared(int arg)
> 同tryAcquire(int arg)方法，区别在于本方法用于共享模式

- protected boolean tryReleaseShared(int arg)
> 同 tryRelease(int arg)方法，区别在于本方法用于共享模式

---

## 获取信号量失败以后入队 （FIFO等待队列）

等待队列是"CLH"锁队列的变体。CLH锁通常用于自旋锁。我们使用FIFO队列用于阻塞锁，但是使用相同的基本策略来保存在队列中线程的控制信息。
每个节点的状态 *waitStatus* 用于跟踪线程是否应该被阻塞。当前置节点 *pre* 释放锁资源的时候会发出信号，队列中每个节点都充当通知器，
并包含一个线程。但是状态字段并不能保证线程是否被授予锁。如果线程是队列中的第一个线程，它可能会获取到锁，但是并不一定成功（公平锁与非公平锁）。

新增节点，只需要在队列的末尾用一次<font color='#f00'>原子操作</font>进行拼接。退出队列，只需要设定 *head* 字段

```
      +------+  prev +-----+       +-----+
 head |      | <---- |     | <---- |     |  tail
      +------+       +-----+       +-----+
```

*pre* 主要用于处理节点的取消。如果当前节点被取消，其后续节点将重新链到未取消的前置节点。

*next* 节点实现阻塞机制。线程信息保存在各自的节点中，前置节点通过遍历下一个节点去通知与唤醒。ps：增加这个节点是为了优化遍历

*head* 队列必须要head节点才能使用，但是我们在初始化的时候不会创建head，没用争用则浪费空间。在第一次有争用且入队的时候构造head。

Cancellation introduces some conservatism to the basic algorithms.  Since we must poll for cancellation of other nodes, we can miss noticing whether a cancelled node is ahead or behind us. This is dealt with by always unparking successors upon cancellation, allowing them to stabilize on a new predecessor, unless we can identify an uncancelled predecessor who will carry this responsibility.

---

## 源码分析

### 独占锁与共享锁方法实现对比

**独占锁** | **共享锁** 
—|—
tryAcquire(int arg) | tryAcquireShared(int arg) 
tryAcquireNanos(int arg, long nanosTimeout)	| tryAcquireSharedNanos(int arg, long nanosTimeout) 
acquire(int arg)| acquireShared(int arg)|
acquireQueued(final Node node, int arg)|doAcquireShared(int arg)
acquireInterruptibly(int arg)|acquireSharedInterruptibly(int arg)
doAcquireInterruptibly(int arg)|doAcquireSharedInterruptibly(int arg)
doAcquireNanos(int arg, long nanosTimeout)|doAcquireSharedNanos(int arg, long nanosTimeout)
release(int arg)|releaseShared(int arg)
tryRelease(int arg)|tryReleaseShared(int arg)
-|doReleaseShared()



### Node.class
```java
static final class Node {
    /** 共享模式 */
    static final Node SHARED = new Node();
    /** 独占模式 */
    static final Node EXCLUSIVE = null;

    /** waitStatus 的取消状态。节点在进入取消状态则不再改变 */
    static final int CANCELLED =  1;
    /** 后继节点处于等待队列中，当前节点线程如果释放了锁，或被取消了，则会通知后继节点，使后继节点线程运行 */
    static final int SIGNAL    = -1;
    /** 节点处于等待队列中，节点线程等待在Condition上，当其他线程对Condition调用了signal()方法后，该节点从等待队列中转移到同步队列中，加入到对同步状态的获取中 */
    static final int CONDITION = -2;
    /** 表示下一次的共享状态会被无条件的传播下去 */
    static final int PROPAGATE = -3;

    /**
     * 节点状态，也就是以上 CANCELLED，SIGNAL，CONDITION，PROPAGATE，以及0（初始值）
     * 对于普通节点，初始值为0
     * 对于条件节点，初始值为 -2 CONDITION
     * 对于当前值的修改需要使用CAS
     */
    volatile int waitStatus;

    /**
     * 前置节点
     */
    volatile Node prev;

    /**
     * 后置节点，后置节点为null不代表它就是最后一个节点（原因请见enq方法）
     */
    volatile Node next;

    /**
     * 当前节点包含的线程
     */
    volatile Thread thread;

    /**
     * 等待队列（非同步队列）的后继节点。如果当前节点是共享的，那么这个字段将是一个SHARED常量
     */
    Node nextWaiter;

    /**
     * 返回是否是共享模式
     */
    final boolean isShared() {
        return nextWaiter == SHARED;
    }

    /**
     * 查看当前节点的前置节点
     */
    final Node predecessor() throws NullPointerException {
        Node p = prev;
        if (p == null)
            throw new NullPointerException();
        else
            return p;
    }

    Node() {    // Used to establish initial head or SHARED marker
    }

    Node(Thread thread, Node mode) {     // Used by addWaiter
        this.nextWaiter = mode;
        this.thread = thread;
    }

    Node(Thread thread, int waitStatus) { // Used by Condition
        this.waitStatus = waitStatus;
        this.thread = thread;
    }
}
```

### AQS部分源码
```java
public abstract class AbstractQueuedSynchronizer
    extends AbstractOwnableSynchronizer
    implements java.io.Serializable {
    /**
     * 同步队列中头节点
     */
    private transient volatile Node head;
    
    /**
     * 同步队列中尾节点
     */
    private transient volatile Node tail;
    
    /**
     * 信号量
     */
    private volatile int state;
    
    /**
     * 获取当前信号量的值
     */
    protected final int getState() {
        return state;
    }
    
    /**
     * 设置信号量的值
     */
    protected final void setState(int newState) {
        state = newState;
    }
    
    /**
     * CAS修改信号量的值
     */
    protected final boolean compareAndSetState(int expect, int update) {
        // See below for intrinsics setup to support this
        return unsafe.compareAndSwapInt(this, stateOffset, expect, update);
    }
    
    // 
    static final long spinForTimeoutThreshold = 1000L;
    
    /**
     * 入队操作
     * @param node the node to insert
     * @return node's predecessor
     */
    private Node enq(final Node node) {
        for (;;) {
            Node t = tail;
            if (t == null) { // 尾节点为空，则初始化节点
                // 设置一个空的首节点，不使用
                if (compareAndSetHead(new Node()))
                    tail = head;
            } else {
                // 将当前节点的前置节点置为 t 也就是尾节点
                node.prev = t;
                // 通过CAS，将尾节点的后置节点设置为当前节点，如果不成功则自选再来一次
                // 这里也就是前面说的节点的next为空，并不代表其为尾节点
                if (compareAndSetTail(t, node)) {
                    t.next = node;
                    return t;
                }
            }
        }
    }
    
    /**
     * 创建给定节点并入队
     *
     * @param mode Node.EXCLUSIVE for exclusive, Node.SHARED for shared
     * @return the new node
     */
    private Node addWaiter(Node mode) {
        Node node = new Node(Thread.currentThread(), mode);
        // 先尝试直接链到tail后面
        Node pred = tail;
        if (pred != null) {
            node.prev = pred;
            if (compareAndSetTail(pred, node)) {
                pred.next = node;
                return node;
            }
        }
        // 失败后老老实实入队
        enq(node);
        return node;
    }
    
    /**
     * 如果存在后继节点，则唤醒后继节点
     */
    private void unparkSuccessor(Node node) {
        /*
         * 修改当前节点的waitStatus
         * 调用当前方法的地方都是对当前节点释放锁了
         * 所以此处可以直接将节点状态改为0，
         * 失败代表这其他线程已经将其置为0，比如，当前线程执行完毕的同时关闭线程池，就会造成这里的cas失败。
         */
        int ws = node.waitStatus;
        if (ws < 0)
            compareAndSetWaitStatus(node, ws, 0);

        /*
         * 先从后继找，如果后继为null，则从tail节点开始往前找
         * 找到实际需要被唤醒的节点。
         */
        Node s = node.next;
        if (s == null || s.waitStatus > 0) {
            s = null;
            for (Node t = tail; t != null && t != node; t = t.prev)
                if (t.waitStatus <= 0)
                    s = t;
        }
        if (s != null)
            // 底层调用unsafe唤醒线程
            LockSupport.unpark(s.thread);
    }
    
    /**
     *  独占模式获取锁的方法，需要自己实现
     */
    protected boolean tryAcquire(int arg) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * 尝试以独占模式获取信号量，若被中断则终止，若超过指定时间则返回失败。
     * 1、先检查线程状态，若中断则终止
     * 2、调用一次 tryAcquire(int args) 方法若成功则返回true，
     * 否则进行排队调用doAcquireNanos(int arg, long nanosTimeout)
     */
    public final boolean tryAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        return tryAcquire(arg) ||
            doAcquireNanos(arg, nanosTimeout);
    }
    
    
     /**
     * 独占模式获取信号量
     */
    private boolean doAcquireNanos(int arg, long nanosTimeout)
            throws InterruptedException {
        // 如果参数不符合规定则返回失败
        if (nanosTimeout <= 0L)
            return false;
        // 终止时间
        final long deadline = System.nanoTime() + nanosTimeout;
        // 包装成独占模式节点入队
        final Node node = addWaiter(Node.EXCLUSIVE);
        boolean failed = true;
        try {
            for (;;) {
                // 寻找当前节点的前置节点
                final Node p = node.predecessor
                // 如果前置节点是head节点，且获取锁成功
                if (p == head && tryAcquire(arg)) {
                    // 将当前节点设置为首节点
                    setHead(node);
                    // 将前置节点到后继（也就是当前节点设置为null），以帮助GC
                    p.next = null; // help GC
                    // 不执行 finally 中的 取消获取信号量动作
                    failed = false;
                    return true;
                }
                // 计算是否超时
                nanosTimeout = deadline - System.nanoTime();
                if (nanosTimeout <= 0L)
                    // 超时返回false
                    return false;
                /*
                 * 对shouldParkAfterFailedAcquire(Node pred,Node node)方法保持疑问，不会出现线程不安全对情况吗？
                 */
                // 若当前方法需要被挂起，且超时时间大于1秒（spinForTimeoutThreshold） 则被挂起
                if (shouldParkAfterFailedAcquire(p, node) &&
                    nanosTimeout > spinForTimeoutThreshold)
                    LockSupport.parkNanos(this, nanosTimeout);
                if (Thread.interrupted())
                    throw new InterruptedException();
            }
        } finally {
            if (failed)
                // 取消获取信号量动作，后续分析此方法
                cancelAcquire(node);
        }
    }
    
    
    /**
     * 以独占模式获取信号量，忽略中断。
     * 1、调用tryAcquire(int args)方法，若成功则返回
     * 2、第一步失败则入队
     *
     */
    public final void acquire(int arg) {
        if (!tryAcquire(arg) &&
            acquireQueued(addWaiter(Node.EXCLUSIVE), arg))
            //中断当前线程
            selfInterrupt();
    }
    
    /**
     * 
以独占不间断模式获取队列中已存在的线程。由条件等待方法和获取使用。
     *
     * @param node the node
     * @param arg the acquire argument
     * @return {@code true} if interrupted while waiting
     */
    final boolean acquireQueued(final Node node, int arg) {
        boolean failed = true;
        try {
            boolean interrupted = false;
            for (;;) {
                final Node p = node.predecessor();
                if (p == head && tryAcquire(arg)) {
                    setHead(node);
                    p.next = null; // help GC
                    failed = false;
                    return interrupted;
                }
                if (shouldParkAfterFailedAcquire(p, node) &&
                    parkAndCheckInterrupt())
                    interrupted = true;
            }
        } finally {
            if (failed)
                cancelAcquire(node);
        }
    }
    
    
    
}

```