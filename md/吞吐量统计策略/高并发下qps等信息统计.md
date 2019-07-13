## 高并发下QPS，TPS，RT等信息统计

##### 在目前的大背景下，并发量的高低与经济效益直接挂钩，所以如何在使服务更稳定，能接受更高的吞吐量是当前热议的一个话题。故，本次分享的内容是在高并发下的QPS，TPS，RT等信息的统计。



### 如何保证数据的正确性

- 加锁阻塞（Lock）

  ​	优点：确保数据的完全正确

  ​	缺点：对于高并发是逆向优化

- 自旋CAS统计（AtomicInteger）

  ​	优点：不阻塞

  ​	缺点：当并发量高的情况下，CPU会飙高，同样对高并发又负面影响

- 热点数据分散的自旋CAS统计（LongAdder）

  ​	优点：不阻塞，同样不会飙高cpu，对高并发造成负面影响

  ​	缺点：相比于以上两种方式，占用内存多，空间复杂度高

##### LongAdder与AtomicInteger在高并发下效率对比

[传送门](https://github.com/Never12581/study-demo/blob/master/md/吞吐量统计策略/LongAdder与AtomicLong速度对比.md)

##### LongAdder源码分析

[传送门](https://github.com/Never12581/study-demo/blob/master/md/吞吐量统计策略/LongAdder源码分析.md)

### 如何保证数据的可靠性

- 滑动窗口算法

  ​	这里描述的并非是tcp协议中的滑动窗口算法。这里的滑动窗口算法是指，将数据统计单位分的更小，在某个时刻去查看单位时间吞吐量会更为符合标准，也就是更可靠。简单来说，对于数据的采样越多，数据的可靠性越高。

##### Sentinel中滑动窗口实现
[传送门](https://github.com/Never12581/study-demo/blob/master/md/吞吐量统计策略/sentinel中滑动窗口的实现.md)

##### Hystrix中滑动窗口实现
[传送门](https://github.com/Never12581/study-demo/blob/master/md/吞吐量统计策略/Hystrix中滑动窗口的实现.md)


