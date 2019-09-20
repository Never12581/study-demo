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

### info:Java配置信息工具

jinfo （Configuration Info For Java）的作用是实时地查看喝调整虚拟机各项参数。使用jps -v 可以查看虚拟机启动时显示指定的参数列表，但是如果想知道未被显式指定的参数的系统默认值，可以使用jinfo -flag 进行查询（仅限JDK 1.6或以上版本）

