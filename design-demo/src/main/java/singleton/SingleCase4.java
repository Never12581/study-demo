package singleton;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 21:12 2019/4/14
 */
public class SingleCase4 {

    /**
     * 劣势：实现复杂
     * 优势：高并发场景下线程安全
     *
     * 解释；
     * synchronized 可以保证可见性，原子性，有序性
     * 个人理解 synchronized 的有序性是 在锁边界保证 有序性，而非volatile般，能防止指令重排序
     * 加入volatile的意义在于防止汇编指令重排序。
     *
     * 比如 在new 对象的过程中 有三个步骤，1、开辟内存空间；2、执行构造方法；3、将指针执行当前内存空间
     * 若不加volatile 则 上述三个步骤的执行顺序可能是 1，3，2。
     * 假如获取到锁的线程执行了1和3，此时cpu时间片用完了，cpu缓存刷回到主存
     * 此时其他线程判断 singleCase4 是否为空，singleCase4不为空，调用singleCase4的方法，
     * 但是此时singleCase4并未初始化完毕，则报错！
     */

    private volatile static SingleCase4 singleCase4 = null;

    private SingleCase4() {
    }

    private static SingleCase4 getInstance() {
        if (singleCase4 == null) {
            synchronized (SingleCase4.class) {
                if (singleCase4 == null) {
                    singleCase4 = new SingleCase4();
                }
            }
        }
        return singleCase4;
    }

}
