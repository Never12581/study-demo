
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
每个节点的状态*waitStatus*用于跟踪线程是否应该被阻塞。当前置节点*pre*释放锁资源的时候会发出信号，队列中每个节点都充当通知器，
并包含一个线程。但是状态字段并不能保证线程是否被授予锁。如果线程是队列中的第一个线程，它可能会获取到锁，但是并不一定成功（公平锁与非公平锁）。

新增节点，只需要在队列的末尾用一次<font color='#f00'>原子操作</font>进行拼接。退出队列，只需要设定*head*字段

```
      +------+  prev +-----+       +-----+
 head |      | <---- |     | <---- |     |  tail
      +------+       +-----+       +-----+
```

*pre*主要用于处理节点的取消。如果当前节点被取消，其后续节点将重新链到未取消的前置节点。

*next*节点实现阻塞机制。线程信息保存在各自的节点中，前置节点通过遍历下一个节点去通知与唤醒。ps：增加这个节点是为了优化遍历

*head*；队列必须要head节点才能使用，但是我们在初始化的时候不会创建head，没用争用则浪费空间。在第一次有争用且入队的时候构造head。

Cancellation introduces some conservatism to the basic algorithms.  Since we must poll for cancellation of other nodes, we can miss noticing whether a cancelled node is ahead or behind us. This is dealt with by always unparking successors upon cancellation, allowing them to stabilize on a new predecessor, unless we can identify an uncancelled predecessor who will carry this responsibility.


---

## 源码分析

- CANCELLED，值为1 。场景：当该线程等待超时或者被中断，需要从同步队列中取消等待，则该线程被置1，即被取消（这里该线程在取消之前是等待状态）。节点进入了取消状态则不再变化

- SIGNAL，值为-1。场景：后继的节点处于等待状态，当前节点的线程如果释放了同步状态或者被取消（当前节点状态置为-1），将会通知后继节点，使后继节点的线程得以运行

- CONDITION，值为-2。场景：节点处于等待队列中，节点线程等待在Condition上，当其他线程对Condition调用了signal()方法后，该节点从等待队列中转移到同步队列中，加入到对同步状态的获取中

- PROPAGATE，值为-3。场景：表示下一次的共享状态会被无条件的传播下去

- INITIAL，值为0，初始状态。

```java
static final class Node {
    /** 共享模式 */
    static final Node SHARED = new Node();
    /** 独占模式 */
    static final Node EXCLUSIVE = null;

    /** waitStatus 的取消状态。节点在进入取消状态则不再改变 */
    static final int CANCELLED =  1;
    /** 后继节点处于等待队列中，当前节点线程如果释放了锁，或被取消了，则会通知后继即诶单，使后继节点线程运行 */
    static final int SIGNAL    = -1;
    /** waitStatus value to indicate thread is waiting on condition */
    static final int CONDITION = -2;
    /**
     * waitStatus value to indicate the next acquireShared should
     * unconditionally propagate
     */
    static final int PROPAGATE = -3;

    /**
     * Status field, taking on only the values:
     *   SIGNAL:     The successor of this node is (or will soon be)
     *               blocked (via park), so the current node must
     *               unpark its successor when it releases or
     *               cancels. To avoid races, acquire methods must
     *               first indicate they need a signal,
     *               then retry the atomic acquire, and then,
     *               on failure, block.
     *   CANCELLED:  This node is cancelled due to timeout or interrupt.
     *               Nodes never leave this state. In particular,
     *               a thread with cancelled node never again blocks.
     *   CONDITION:  This node is currently on a condition queue.
     *               It will not be used as a sync queue node
     *               until transferred, at which time the status
     *               will be set to 0. (Use of this value here has
     *               nothing to do with the other uses of the
     *               field, but simplifies mechanics.)
     *   PROPAGATE:  A releaseShared should be propagated to other
     *               nodes. This is set (for head node only) in
     *               doReleaseShared to ensure propagation
     *               continues, even if other operations have
     *               since intervened.
     *   0:          None of the above
     *
     * The values are arranged numerically to simplify use.
     * Non-negative values mean that a node doesn't need to
     * signal. So, most code doesn't need to check for particular
     * values, just for sign.
     *
     * The field is initialized to 0 for normal sync nodes, and
     * CONDITION for condition nodes.  It is modified using CAS
     * (or when possible, unconditional volatile writes).
     */
    volatile int waitStatus;

    /**
     * Link to predecessor node that current node/thread relies on
     * for checking waitStatus. Assigned during enqueuing, and nulled
     * out (for sake of GC) only upon dequeuing.  Also, upon
     * cancellation of a predecessor, we short-circuit while
     * finding a non-cancelled one, which will always exist
     * because the head node is never cancelled: A node becomes
     * head only as a result of successful acquire. A
     * cancelled thread never succeeds in acquiring, and a thread only
     * cancels itself, not any other node.
     */
    volatile Node prev;

    /**
     * Link to the successor node that the current node/thread
     * unparks upon release. Assigned during enqueuing, adjusted
     * when bypassing cancelled predecessors, and nulled out (for
     * sake of GC) when dequeued.  The enq operation does not
     * assign next field of a predecessor until after attachment,
     * so seeing a null next field does not necessarily mean that
     * node is at end of queue. However, if a next field appears
     * to be null, we can scan prev's from the tail to
     * double-check.  The next field of cancelled nodes is set to
     * point to the node itself instead of null, to make life
     * easier for isOnSyncQueue.
     */
    volatile Node next;

    /**
     * The thread that enqueued this node.  Initialized on
     * construction and nulled out after use.
     */
    volatile Thread thread;

    /**
     * Link to next node waiting on condition, or the special
     * value SHARED.  Because condition queues are accessed only
     * when holding in exclusive mode, we just need a simple
     * linked queue to hold nodes while they are waiting on
     * conditions. They are then transferred to the queue to
     * re-acquire. And because conditions can only be exclusive,
     * we save a field by using special value to indicate shared
     * mode.
     */
    Node nextWaiter;

    /**
     * Returns true if node is waiting in shared mode.
     */
    final boolean isShared() {
        return nextWaiter == SHARED;
    }

    /**
     * Returns previous node, or throws NullPointerException if null.
     * Use when predecessor cannot be null.  The null check could
     * be elided, but is present to help the VM.
     *
     * @return the predecessor of this node
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
