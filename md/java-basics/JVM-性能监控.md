# 虚拟机性能监控与故障处理工具

## JDK命令行工具

### jsp：虚拟机进程状况工具

jsp（JVM Process Status Tool）名字与ps相似，功能也与ps相近，可以列出正在运行的虚拟机进程，并显示虚拟机主类（Main Class，main（）所在的类）名称以及这些进程的本地虚拟机唯一ID（Local Virtual Machine Identifier，LVMID）。

| 选项 | 作用                                           |
| ---- | ---------------------------------------------- |
| -q   | 只输出pid，即LVMID，省略主类名称               |
| -m   | 输出虚拟机进程启动时传递给主类main()函数的参数 |
| -l   | 输出主类的全名，如执行的是jar，则会输出jar路径 |
| -v   | 输出虚拟机进程启动JVM参数                      |

### jstat：虚拟机统计信息监控工具

jstat（JVM Statistics Monitoring Tool）是用于监视虚拟机各种运行状态信息的命令行工具。它可以显示本地或远程 虚拟机进程中的类装载、内存、垃圾收集、JIT编译等运行数据，在没有GUI图形界面，只提供了纯文本控制台环境等服务器上，它将是运行期定位虚拟机性能问题的首选工具。

jstat命令格式如下：

```shell
jstat [ option vimd [interval[s|ms] [count]]]
```

对于命令格式中的VMID与LVMID需要说明一下：如果是本地虚拟机进程，则VMID与LVMID是一致的，若是远程虚拟机进程，则VMID的格式如下：

```shell
[protocol:][//]lvmid[@hostname[:port]/servername]
```

参数intercal喝count代表查询间隔与次数，若省略这两个参数，说明只查询一次。假设每个250ms 查询一次 进程6829的垃圾收集情况，合共查询20次则命令如下：

```shell
jstat -gc 6829 250 20
```

选项option代表着用户希望查询的虚拟机信息，主要分为三类：类装载、垃圾收集、运行期编译状况，具体如下表

| 选项              | 作用                                                         |
| ----------------- | ------------------------------------------------------------ |
| -class            | 监视类装载，卸载数量、总空间以及类装载所耗费时间             |
| -gc               | 监视java堆状况，包括Eden区、两个survivor区、老年代、永久代等的容量、已用空间、GC时间合计等信息 |
| -gccapacity       | 监视内容与-gc基本相同，但是输出主要关注Java堆各个区域使用到的最大、最小空间 |
| -gcutil           | 监视内容与-gc基本相同，输出主要关注已使用空间占用总空间的百分比 |
| -gccause          | 与-gcutil基本相同，额外输出上次gc原因                        |
| -gcnew            | 监视新声代gc情况                                             |
| -gcnewcapacity    | 与-gcnew基本相同，输出主要关注使用到的最大、最小空间         |
| -gcold            | 监视老年代gc情况                                             |
| -gcoldcapacity    | 与-gcold基本相同，输出主要关注使用到的最大、最小空间         |
| -gcpermcapacity   | 输出永久代使用到的最大最小空间                               |
| -compiler         | 输出JIT编译器编译过的方法、耗时信息                          |
| -printcompilation | 输出已经被JIT编译的方法                                      |

### jinfo:Java配置信息工具

jinfo （Configuration Info For Java）的作用是实时地查看喝调整虚拟机各项参数。使用jps -v 可以查看虚拟机启动时显示指定的参数列表，但是如果想知道未被显式指定的参数的系统默认值，可以使用jinfo -flag 进行查询（仅限JDK 1.6或以上版本）

jinfo命令格式：

```shell
jinfo | option | pid
```

执行样例：查询CMSinitiationOccupancyFraction

```shell
jinfo CMSinitiationOccupancyFraction 6628
```

### jmap:Java内存映像工具

jamp（Memory Map for Java）命令用于生成堆转储快照（一般称为heapdump或dump文件）。如果不是用jmap命令，想要获取Java堆转储快照，可以在启动命令中添加 -XX:+HeapDumpOnOutOfMemoryError参数，可以让虚拟机在oom的时候自动生成dump文件；通过-XX:+HeapDumpOnCtrlBreak参数则可以使用 [Ctrl]+[Break]键让虚拟机生成dump文件，又捉着在Linux系统下通过kil -3 命令发送进程退出信号，也能拿到dump文件。

jmap出了获取dump文件，还可以查询finalize执行队列、Java堆和永久代的详细信息，如空间使用率、当前使用哪种收集器等。

和jinfo命令一样，jmap有不少功能在windows平台下是受限的，出了生成dump文件的-dump选项和用于查看每个类的实例、空间占用统计的-histo选项在所有操作系统都提供之外，其余选项都只能在Linux/Solaris下使用。

jmap命令格式：

```shell
jmap [option] vmid
```

option选项都合法值与具体含义如下表

| 选项           | 作用                                                         |
| -------------- | ------------------------------------------------------------ |
| -dump          | 生成Java堆快照。格式为：-dump:[live,]format=b,file=<filename>,其中live子参数说明是否只dump出存活对象 |
| -finalizerinfo | 显示在F-Queue中等待Finalizer线程执行finalize方法等对象。仅在Linux/Solaris平台下有效 |
| -heap          | 显示Java堆详细信息，如使用哪种回收器、参数配置、分代状况等。仅在Linux/Solaris平台下有效 |
| -histo         | 显示堆中对象统计信息，包括类、实例数量、合计容量             |
| -permstat      | 以ClassLoader为统计口径显示永久代内存状态。仅在Linux/Solaris平台下有效 |
| -F             | 当虚拟机进程堆-dump选项没有响应时，可以使用该选项强制生成dump快照。仅在Linux/Solaris平台下有效 |

使用示例

```shell
jmap -dump :format=b,file=eclipse.bin 34500
```

### Jhat : 虚拟机堆转储快照分析工具

Sun JDK提供jhat（JVM Heap Analysis Tool）命令与jmap搭配使用，来分析jmap生成堆转储快照。jhat内置了一个微型http/html服务器，生成dump文件的分析结构后，可以在浏览器中查看。

jhat命令格式：

```shell
jhat | dumpFileName | port
```

如：

```shell
jhat eclipse.bin 7001
```

启动成功后则可以在浏览器中键入 http://localhost:7001 来查看。

分析结果默认以包为单位进行分组显示，分析内存泄漏问题主要会使用到其中的“Heap Histogram”（与jmap-histo功能一样）与OQL页签的功能，前者可以找到内存中总容量最大的对象，后者是标准的对象查询语言，使用类似SQL语法对内存中的对象进行查询统计。

### jstack：Java堆栈跟踪工具

jstack （Stack Trace For Java） 命令用于生成虚拟机当前时刻的线程快照（一般被称为threaddump或者javacore文件）。线程快照就是当前虚拟机内每一条线程正在执行的方法堆栈的集合，生成线程快照的主要目的是定位线程出现长时间停顿的原因，如线程间死锁、死循环、请求外部资源导致的长时间等到等都是导致线程长时间停顿的常见原因。线程出现停顿的时候通过jstack来查看各个线程的调用堆栈，就可以知道没有响应的线程到底在后台做些什么事情，或者等待什么资源。

jstack命令格式：

```shell
jstack [option] vmid
```

option选项的合法值与具体含义如下表：

| 选项 | 作用                                         |
| ---- | -------------------------------------------- |
| -F   | 当正常输出的请求不被响应时，强制输出线程堆栈 |
| -l   | 除堆栈外，显示关于锁的附加信息               |
| -m   | 如果调用到本地方法的话，可以显示C/C++的堆栈  |

### HSDIS：JIT生成代码反汇编

HSDIS是Sun官方推荐的HotSpot虚拟机JIT编译代码的反汇编插件，包含在HotSpot虚拟机源码中，但是没有提供编译后的程序。此处不过多介绍

## JDK可视化工具（待续）

JDK中除了提供大量的命令行工具外，还有两个功能强大的可视化工具：JConsole和VisualVM，这两个工具是JDK的正式成员。

### JConsole：Java监视与管理控制台

JConsole（Java Monitoring And Management Console）是一种基于JMX的可视化监视、管理工具。它管理部分的功能是针对JMX MBean进行管理，由于MBean可以使用代码、中间件服务器的管理控制台或者所有符合JMX规范的软件进行访问。

### VisualVM：多合一故障处理工具

VisualVM（All-in-one Java Troubleshooting Tool）是目前为止随JDK发布的功能最强大的运行监视和故障处理程序。除却运行监视、故障处理外还有诸如性能分析等功能。















