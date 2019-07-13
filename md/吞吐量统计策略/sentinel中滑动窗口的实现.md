sentinel中获取当前桶的源码简单分析

```java
public class LeapArray{
    /**
     * 获取当前时间戳所在的桶
     * 若时间戳合法则返回当前窗口，若时间戳不合理(这里指时间戳小于0的情况)返回null
     */
    public WindowWrap<T> currentWindow(long timeMillis) {
        if (timeMillis < 0) {
            return null;
        }
        // 计算得到当前时间戳应该在哪个桶
        int idx = calculateTimeIdx(timeMillis);
        // 计算当前时间戳，所在的桶的开始时间戳
        long windowStart = calculateWindowStart(timeMillis);
    
        /*
         *
         * 从给定的数组中，获取桶对象
         * (1) 
         * bucket不存在，则创建一个新的bucket，且用cas将循环数组当前index赋值
         * (2) 
         * 若桶是最新的，则将当前的桶返回
         * (3) 
         * 若桶是失效的，将当前的失效的桶的统计数据重置，将桶的开始时间赋值为新的开始时间
         * 
         */
        while (true) {
            WindowWrap<T> old = array.get(idx);
            if (old == null) {
                /*
                 *     B0       B1      B2    NULL      B4
                 * ||_______|_______|_______|_______|_______||___
                 * 200     400     600     800     1000    1200  timestamp
                 *                             ^
                 *                          time=888
                 *            bucket is empty, so create new and update
                 *
                 * 如果当前桶为null，则创建一个新的对象赋值给当前桶，
                 * 能且仅能单个线程更新成功，其他线程让出时间片。
                 */
                WindowWrap<T> window = new WindowWrap<T>(windowLengthInMs, windowStart, newEmptyBucket());
                if (array.compareAndSet(idx, null, window)) {
                    // 通过CAS将对象赋值进当前桶，若赋值成功，则将当前桶返回
                    return window;
                } else {
                    // 存在竞争，则当前线程让出时间片
                    Thread.yield();
                }
            } else if (windowStart == old.windowStart()) {
                /*
                 *     B0       B1      B2     B3      B4
                 * ||_______|_______|_______|_______|_______||___
                 * 200     400     600     800     1000    1200  timestamp
                 *                             ^
                 *                          time=888
                 *            startTime of Bucket 3: 800, so it's up-to-date
                 *
                 * 如果当前桶的开始时间与我们计算所得的开始时间一致，就直接将当前桶返回
                 */
                return old;
            } else if (windowStart > old.windowStart()) {
                /*
                 *   (old)
                 *             B0       B1      B2    NULL      B4
                 * |_______||_______|_______|_______|_______|_______||___
                 * ...    1200     1400    1600    1800    2000    2200  timestamp
                 *                              ^
                 *                           time=1676
                 *          startTime of Bucket 2: 400, deprecated, should be reset
                 *
                 *
                 * 当计算得出的桶的开始时间小于当前计算得出的开始时间
                 * 证明当前桶过期了，
                 * 则将桶清空且将开始时间设置为当前桶的开始时间
                 * 这里比较难以使用cas实现，故直接使用锁
                 * 同样，仅有单个线程可以操作成功，其他线程均放出时间片
                 */
                if (updateLock.tryLock()) {
                    try {
                        return resetWindowTo(old, windowStart);
                    } finally {
                        updateLock.unlock();
                    }
                } else {
                    Thread.yield();
                }
            } else if (windowStart < old.windowStart()) {
                // 这是一种不可能的情况！
                return new WindowWrap<T>(windowLengthInMs, windowStart, newEmptyBucket());
            }
        }
    }
}
```