# hystrix 综览
## 什么是hystrix？
- 在分布式系统，我们一定会依赖各种服务，那么这些个服务一定会出现失败的情况，Hystrix就是这样的一个工具，它通过提供了逻辑上延时和错误容忍的解决力来协助我们完成分布式系统的交互。Hystrix 通过分离服务的调用点，阻止错误在各个系统的传播，并且提供了错误回调机制，这一系列的措施提高了系统的整体服务弹性。

### Hystrix旨在执行以下操作：
- 通过第三方客户端库访问（通常通过网络）依赖关系，以防止和控制延迟和故障。
- 在复杂的分布式系统中停止级联故障。
- 快速失败并迅速恢复。
- 在可能的情况下，后退并优雅地降级。
- 实现近实时监控，警报和操作控制。

### Hystrix解决了什么问题？
- 复杂分布式体系结构中的应用程序具有许多依赖关系，每个依赖关系在某些时候都将不可避免地失败。如果主机服务未与这些外部故障隔离，则可能拖垮主机服务（如耗尽tomcat所有线程数）。


### 主要功能

- 熔断降级
- 请求缓存
- 请求折叠

#### 1、熔断与降级
![ 这里需要文字吗？](https://raw.githubusercontent.com/wiki/Netflix/Hystrix/images/hystrix-command-flow-chart.png "hystrix 熔断降级工作流程 ")
- 线程池隔离模式
- 信号量模式
- 断路器
  
---

### 线程池隔离
#### 简单理解：
    public class TestMain {
        // hystrix 线程池
        static ExecutorService hystrixThreadPool = Executors.newSingleThreadExecutor();
        // fallback 线程池
        static ExecutorService fallbackThreadPool = Executors.newSingleThreadExecutor();
        public static void main(String[] args) {
            // 超时时间
            long timeout = 50;
            Future future = hystrixThreadPool.submit(() -> {
                // 模拟任务
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
            try {
                future.get(timeout,TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                // 当前仅模拟超时情况
                // 正常流程线程取消
                future.cancel(true);
                // 执行fallback线程
                fallbackThreadPool.submit(()->{
                    // do something fallback
                });
            }
        }
    }
#### 原理：
#### 优势：
#### 劣势：

---

### 信号量
#### 原理：
#### 优势：
#### 劣势：

---

### 断路器
    详情请见 com.netflix.hystrix.HystrixCircuitBreaker 


#### 2、请求缓存 （待续）
#### 3、请求降级 （待续）



