## Java内存模型（Java Memory Model，JMM）

### 主内存与工作内存

Java内存模型规定了所有的变量都存储在主内存（Main Memory）中。每个线程还有自己的工作内存（Working Memory），线程的工作内存中保存了被该线程使用到的变量的主内存副本拷贝，线程对变量的所有操作（读取、赋值等）都必须在工作内存中完成，而不能直接读写主内存中的变量。不同线程之间也无法直接访问对方工作内存中的变量，线程见变量值的传递均需要通过主内存来完成。如下图所示：

![工作内存与主内存之间的关系](https://github.com/Never12581/study-demo/blob/master/other-file/picture/java-basics/%E5%B7%A5%E4%BD%9C%E7%BA%BF%E7%A8%8B%EF%BC%8C%E4%B8%BB%E5%86%85%E5%AD%98%E5%85%B3%E7%B3%BB.jpg?raw=true)



### 内存见交互操作

关于主内存与工作内存之间具体的交互协议，即一个变量如何从主内存拷贝到工作内存、如何从工作内存同步回主内存之类的实现细节，Java内存模型定义了以下8种操作来完成，虚拟机实现时必须保证下面提及的每一种操作都是原子的，不可再分的（对于double，long来说，load，store，read，write操作在某些平台上允许有例外，但是多数虚拟机实现上这些操作也是原子性操作）

- lock（锁定）：作用于主内存变量，它把一个变量标识为一条线程独占状态。
- unlock（解锁）：作用于主内存变量，它把一个处于锁定状态的变量释放出来，释放后的变量才能被其他线程锁定
- read（读取）：作用于主内存变量，它把一个变量的值从主内存中传输到线程的工作内存中，以便随后的load动作使用。
- load（载入）：作用于工作内存的变量，它把read操作从主内存中得到的变量值放入工作内存的变量副本中。
- use（使用）：作用于工作内存的变量，它把工作内存中一个变量的值传递给执行引擎，每当虚拟机遇到一个需要使用到变量的值的字节码指令时将会执行这个操作。
- assign（赋值）：作用于工作内存的变量，它把一个从执行引擎接收到的值赋给工作内存的变量，每当虚拟机遇到一个变量赋值的字节码指令时执行这个操作。
- store（存储）：作用于工作内存的变量，它把一个工作内存中一个变量的值传送到主内存中，以便随后的write操作使用。
- write（写入）：作用于主内存的变量，它把store操作从工作内存中得到的变量值放入主内存变量中。

如果要把一个变量从主内存复制到工作内存，那就要顺序地执行read和load操作，如果要把变量从工作内存同步回主内存，就要顺序地执行store和write操作。Java内存模型只要求上述两个操作必须按顺序执行，但是不需要连续执行。也就是说，read于load之间，store与write之间是可插入其他指令的，如对主内存中的变量a，变量b进行访问时，一种可能的顺序是read a, read b,load b , load a 。除此之外，java内存模型还规定了在执行上述8中基本操作时必须满足如下规则：

1. 不允许read和load，store和write操作之一单独出现。即不允许一个变量从主内存读取了但是工作内存不接受，或者工作内存发起回写但是主内存不接受
2. 不允许一个线程丢弃它最近的assign操作。即变量在工作内存中改变了之后必须把该变化同步回主内存
3. 不允许一个线程无原因（没发生任何assign操作）把数据从线程的工作内存同步回主内存中。
4. 一个新的变量只能从主内存中“诞生”，不允许在工作内存中直接使用额一个未被初始化（load或assign）的变狼，换句话说，就是对一个变量在实施use，store操作之前，必须先执行assign和load 操作
5. 一个变量在同一个时刻只允许一条线程对其进行lock操作，但是lock操作可以被同一条线程重复执行多次，多次lock后，只有执行相同次数的unlock操作，变量才会被解锁
6. 如果对一个变量执行lock操作，那将会清空工作内存中此变量的值，在执行引擎使用这个变量前，需要重新执行load或assign操作初始化变量的值
7. 如果一个对象没有被lock，那不允许对其进行unlock操作，也不允许unlock一个被其他线程锁住的变量
8. 对一个对象执行unlock操作前，必须先把此变量同步回主内存中（执行store，write操作）