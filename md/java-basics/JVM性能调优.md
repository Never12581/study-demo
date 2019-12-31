### jvm启动命令 简述

#### jdk1.7之前版本

| -Xms<size>                                                   | 例如:-Xms2G,设置Heap的初始大小                               |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| -Xmx<size>                                                   | 设置Heap的最大尺寸,建议为最大物理内存的1/3(建议此值不超过12G) |
| -Xss<size>                                                   | 方法栈大小                                                   |
| -Xmn<size>                                                   | 新生代大小                                                   |
| -XX:NewSize=<value>-XX:MaxNewSize=<value>-XX:SurvivorRatio=<value> | 新生代设置.单位为byteSurvivorRatio = Eden/survivor;例如此值为3,则表示,Eden : from : to = 3:1:1;默认值为8 |
| -XX:PermSize=<value>-XX:MaxPermSize=<value>                  | 方法区大小,max值默认为64M                                    |
| -XX:[+ \| -]UseConcMarkSweepGC                               | 打开或关闭基于ParNew + CMS + SerialOld的收集器组合.(-XX:+UseParNewGC) |
| -XX:[+ \| -]UseParallelGC                                    | 在server模式下的默认值(+),表示使用Parallel Scavenge + Serial Old收集器组合 |
| -XX:[+ \| -]UseParallelOldGC                                 | 默认关闭,+表示打开/使用Parallel Scavenge + Parallel Old收集器组合 |
| -XX:PretenureSizeThreshold=<value>                           | 直接晋升为旧生代的对象大小.大于此值的将会直接被分配到旧生代,单位byte |
| -XX:MaxTenuringThreshold=<value>                             | 晋升到旧生代的对象年龄(已经或者即将被回收的次数);每个对象被minor GC一次,它的年龄+1,如果对象的年龄达到此值,将会进入旧生代. |
| -XX:[+ \| -]UseAdaptiveSizePolicy                            | 默认开启;是否动态调整java中堆中各个区域大小以及进入旧生代的年龄;此参数可以方便我们对参数调优,找到最终适合配置的参数. |
| -XX:[+ \| -]HandlePromotionFailure                           | JDK1.6默认开启,是否支持内存分配失败担保策略;在发生Minor GC时，虚拟机会检测之前每次晋升到老年代的平均大小是否大于老年代的剩余空间大小，如果大于，则改为直接进行一次Full GC。如果小于，则查看HandlePromotionFailure设置是否允许担保失败；如果允许，那只会进行Minor GC；如果不允许，则也要改为进行一次Full GC。 |
| -XX:ParallelGCThreads=<value>                                | 并行GC时所使用的线程个数.建议保持默认值(和CPU个数有换算关系). |
| -XX:GCTimeRatio=<value>                                      | GC占JVM服务总时间比.默认为99,即允许1%的GC时间消耗.此参数只在Parallel Scavenge收集器下生效 |
| -XX:CMSInitiatingOccupancyFraction=<value>                   | 设置CMS收集器在旧生代空间适用占比达到此值时,触发GC.          |
| -XX:[+ \| -]UseCMSCompactAtFullCollection                    | 默认开启,表示在CMS收集器进行一次Full gc后是否进行一次内存碎片整理,[原因:CMS回收器会带来内存碎片] |
| -XX:CMSFullGCSBeforeCompaction=<value>                       | 进行多少次FullGC之后,进行一次内存碎片整理.[原因:CMS回收器会带来内存碎片] |