## eureka

拷贝自 https://mp.weixin.qq.com/s/AhRYd0Iwrxb_nsN4F9E9DQ ，加上部分自己理解

### 总结

- 服务端（指配置服务）
  1. 高可用：集群部署，各个服务端之间数据同步

### 组件调用关系

#### 服务提供者

1. 启动后，想注册中心发起register请求，请求注册服务
2. 在运行过程中，定时向注册中心发送renew心跳，证明 服务正常
3. 停止时，向注册中心发起cancel 请求，清空当前服务注册信息

#### 服务消费者

1. 启动后，向注册中心拉取服务注册信息

2. 在运行中，定时向服务拉取注册信息，定时更新服务注册信息

3. 服务发起远程调用：

   消费者从注册中心选择同机房服务提供者发起调用，如果同机房没有对应服务，则根据负载均衡算法选择远程机房服务提供者发起调用

#### 注册中心

1. 启动后，想其他节点拉取服务注册信息
2. 运行过程中，定时运行evict任务，提出没有按时renew的服务（服务异常或网络问题）
3. 运行过程中，接收到的register、renew、cancel请求，都会同至其他注册中心节点

### 数据存储结构

eureka 数据存储分了两层：数据存储层和缓存层。 eureka client 在拉取服务信息时，先从缓存层获取，如果获取不到，再从存储层获取。缓存层存储的数据结构可直接传输到Eureka client ，数据存储层保存的数据结构需要经过加工才能传输到eureka client

#### 数据存储层（双层ConcurrentHashMap）

源码在 AbstractInstanceRegistry.java 

这里用的是一个双层的ConcurrentHashMap，第一层的key是 spring.application.name ，value是 第二层的ConcurrentHashMap ；

第二层的 ConcurrentHashMap 的key 是服务的InstanceId，value是 Lease<InstanceInfo>对象 。其中 key 在同个 spring.application.name 的范围保证唯一，由DataCenterInfo 生成，若生成失败，则使用 hostName。Lease 对象包含服务详情和服务治理相关属性

#### 二级缓存层

源码在 ResponseCacheImpl.java	

一级缓存：ConcurrentHashMap，无过期时间，保存服务信息对外的数据结构

二级缓存：Loading<k,v> ， 本质上是guava 的缓存， 包含失效机制，保存服务队外的数据结构

其中 两个缓存中的 Key 是一个 com.netflix.eureka.registry.Key 对象 ，重写了其 hashcode()  方法，<font color='red'>使不会出现内存泄漏的情况 </font>

##### 删除二级缓存：

1. eureka client 发送 register、renew、cancel 请求并更新registry注册表后，删除二级缓存；
2. dureka server 自身的 Evict Task 剔除服务后，删除二级缓存
3. 二级缓存本身设置了 guava 的失效机制，隔一段时间后自己自动失效

##### 加载二级缓存

1. eurekaclient 发送 getRegister 请求后，如果二级缓存中没有，就出发guava 的load ， 即从registry 中获取原始服务信息，加工后加载至二级缓存
2. eureka server 更新一级缓存的时候，如果二级缓存没有数据，也会出发 guava 的load

##### 更新一级缓存

1.  eureka server 内置了以 TimerTask ，定时将二级缓存中的数据同步到一级缓存中



### 服务注册机制

com.netflix.eureka.registry.AbstractInstanceRegistry#register

服务提供者，消费者，以及服务注册中心自己，启动后都会向注册中心注册服务（服务注册中心配置了向其他注册中心注册）。

注册中心接收到register 请求后：

1. 保存服务信息，将服务信息保存到register中（存储层中）
2. 更新队列，将此时间添加到更新队列中，供 eureka client 增量同步服务信息使用
3. 清空二级缓存，readWriteCacheMap，保证数据一致性
4. 更新阈值，供剔除服务使用
5. 同步服务信息，将此时间同步至其他eureka server 节点

### 服务续约机制

com.netflix.eureka.registry.AbstractInstanceRegistry#renew

服务注册后，要定时（默认30s）向注册中心发送续约请求。

1. 更新服务对象的最近续约时间，即Lease对象的lastUpdateTimestamp;
2. 同步服务信息，将此事件同步至其他的Eureka Server节点。

### 服务注销机制

com.netflix.eureka.registry.AbstractInstanceRegistry#cancel

1. 删除服务信息，将服务信息从存储层registry中删除
2. 更新队列，将此事件添加到更新队列中，供 eureka client 增量同步服务信息使用
3. 清空二级缓存，热啊dWriteCachemap，保证数据一致性
4. 更新阈值，供剔除服务使用
5. 同步服务信息，将此事件同步至其他eureak server 节点

### 服务剔除机制

com.netflix.eureka.registry.AbstractInstanceRegistry#evict

剔除是指 剔除 没有正常下线，有没有续约的服务

1. 判断是否满足剔除条件
2. 找出过期服务
3. 执行剔除

#### 判断是否满足剔除条件

1. 关闭了自我保护（即所有未续约的，都认为是 eureka client 的问题）
2. 如果开启了自我保护，需要进一步判断是 eureka server 的问题还是 eureka client 的问题。通过renewalPercentThreshold 阈值判断，如果eureka client 续约失败的比例大于 该阈值，则认为当前eureka server 除了问题，不进行剔除；如果小于该比例，则认为是 eureka client 出现了问题，则进行剔除

#### 找出过期服务

遍历所有的服务，判断上次续约时间距离当前时间大于阈值就标记为过期。并将这些过期的服务保存到集合中。

#### 剔除服务

在剔除服务之前先计算剔除的数量，然后遍历过期服务，通过洗牌算法确保每次都公平的选择出要剔除的任务，最后进行剔除。

执行剔除服务后：

1. 删除服务信息，从registry中删除服务。
2. 更新队列，将当前剔除事件保存到更新队列中。
3. 清空二级缓存，保证数据的一致性。

### 服务发现机制

com.netflix.eureka.registry.AbstractInstanceRegistry#getApplications ？？？

1. 先从以及缓存里读取获取
   1. 先判断是否开启了一级缓存
   2. 如果开启了一级缓存则从一级缓存中获取，如果没有则从二级缓存中获取
   3. 如果未开启，直接从二级缓存中获取
2. 再从二级缓存中获取
   1. 如果二级缓存中存在，则直接返回
   2. 如果二级缓存中不存在，则先将数据加载到二级缓存，再从二级缓存中获取。加载时需要判断是增量同步还是全量同步，增量同步从recentlyChangedQueue中load，全量同步则是从registry中load

### 服务同步机制

com.netflix.eureka.registry.PeerAwareInstanceRegistryImpl#syncUp

服务同步指eureka server 节点之间的同步服务信息下。包含启动是同步，运行过程中同步。

#### 启动时同步

Eureka Server 启动后，遍历 eurekaClient.getApplications 获取服务信息，并将服务信息注册到自己的registry中。

#### 运行时同步

当 Eureka Server 节点有register、renew 、 cancel 请求进来时，会讲这个请求封装成 TaskHolder 放到 acceptorQueue 队列中，然后经过一些列处理，放到 batchWOrkQueue 中。

TaskExecutor.BatchWorkerRunnable是个线程池，不断的从batchWorkQueue队列中poll出TaskHolder，然后向其他Eureka Server节点发送同步请求。

> 这里省略了两个部分：
>
> 一个是在acceptorQueue向batchWorkQueue转化时，省略了中间的processingOrder和pendingTasks过程。
>
> 另一个是当同步失败时，会将失败的TaskHolder保存到reprocessQueue中，重试处理。

