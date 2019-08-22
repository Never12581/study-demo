此处OOM指的是OutOfMemoryError!

#### Java堆内存溢出

java堆用于存储实例对象，只要不断的创建对象且保证gcroot到对象之间有可达路径来避免垃圾回收机制清楚这些对象，那么在对象数量达到最大堆的容量限制以后会产生内存溢出异常。

> 参数 -XX:+HeapDumpOnOutOfMemoryError 可以让虚拟机在出现内存溢出异常时dump出当前内存堆转转储快照以便之后分析

堆内存在OOM时，一场堆栈信息“java.lang.OutOfMemoryError”会跟着提示“Java heap space”。表明是java堆的问题。

解决该异常步骤；

- 在启动参数中加上 -XX:+HeapDumpOnOutOfMemoryError
- 通过内存映像分析工具（如 eclipse memory analyzer）对dump出来的快照进行分析。确认内存中的对象是否是必要的，即分清楚是内存泄漏（Memory Leak）还是内存溢出（Memory overflow）。
- 如果是内存泄漏，可以通过工具查看泄漏对象到GC Root的引用链。通过引用链猜测问题代码的位置
- 如果是内存溢出，检查启动的堆参数（-Xmx 和 -Xms）能够调大内存；检查代码，是否存在某些对象生命周期过长的情况，及时清除。

#### 虚拟机栈和本地方法栈溢出

对于HotSpot来说，虽然-Xoss参数（设置本地方法栈的大小）存在但是无效，栈容量由-Xss参数设定。

在java虚拟机规范中描述了两种异常的情况：

- 如果线程请求的栈深度大于虚拟机所允许的最大深度，将抛出StackOverflowError异常
- 如果虚拟机在扩展栈时无法申请到足够的内存空间，则抛出OutOfMemoryError异常

然而当栈空间无法被继续分配时，到底是内存太小，还是已使用栈空间太大，其本质是对同一问题的不同描述。

简单测试步骤：

1. 测试一：

   - 在启动参数中加上：-Xss参数，减少栈内存容量
   - 写一个无限嵌套的代码

   结果：抛出StackOverflowError异常

2. 测试二：

   - 在启动参数中加上：-Xss参数，减少栈内存容量
   - 在测试方法中大量增加本地变量表的长度

   结果：抛出StackOverflowError异常

简单结论：在单线程下，无论是栈帧太大还是虚拟机栈容量太小，当内存无法分配的时候，虚拟机抛出的都是StackOverflowError异常

在非单线程的情况下，不断建立线程可以导致OOM，但是线程对象存在于堆内存中，所以也没有证据证明是栈溢出

#### 方法区与运行时常量池溢出

 简单测试步骤：

- 在启动参数上加上 -XX:PermSize=10M -XX:MaxPermSize=10M   

  >  在jdk1.8中已被移除
  >
  > 用 -XX:MetaspaceSize=10M 与 -XX:MaxMetaspaceSize=10M

- 以String.intern()方法举例

得出结论：

- 在jdk1.7之前报错：java.lant.OutOfMemoryError:PermGen space
- 在jdk1.8及之后报错：java.lant.OutOfMemoryError:Metaspace

> 在使用cglib进行增强的时候会生成大量的类，也有可能使元数据空间（方法区）出现oom



#### 直接内存溢出

直接内存可以通过-XX：MaxDirectMemorySize 指定大小，若不指定则默认与Java堆最大值（-Xmx）指定一样。

如果dump出的oom镜像很小，则可考虑直接内存溢出的情况。