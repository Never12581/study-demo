### 零、别人家的优秀文章

https://mp.weixin.qq.com/s?__biz=MzI3MTQ1NzU2NA==&mid=2247484003&idx=1&sn=04a690cb1f6bb5550f5e3f4ecf4f2a6e&chksm=eac0ce10ddb7470649f5a21e0d3ee31169f121d60334c54320c4be2db5cdd47d4e7dfd300834&scene=21#wechat_redirect



### 一、tcp头部解释

![tcp头部](https://images2017.cnblogs.com/blog/822287/201712/822287-20171220194113475-1709447191.png)

**16位源，目的端口号**：标示该段保文来自哪里（源端口）以及要传给哪个上层协议或者应用程序（目的端口）。
进行tcp通信时，一般client时通过系统自动选择的临时端口号，而服务器一般是使用知名服务端口号
或者自己指定的端口号。

**32位序号**：标示一次tcp通信过程（从建立到断开）过程中某一次传输方向上的字节流的每个字节的编号。
假定主机A与B进行tcp通信，A传送给B一个tcp报文段中，序号值被系统初始化为某一个随机值ISN，那么
在传输方向上（A到B），后续所有的报文段中的序号值都会被设定为ISN加上报文段所携带数据的第一个
字节在整个字节流中的偏移量。如，本次报文段传送的数据是整个字节流中的1025～2048字节，故此时
序号值为ISN+1025

**32位确认号**：用作对另一方发送的tcp报文段的响应。其值是收到对方的tcp报文段段序号值+1。假定主机A
与主机B进行tcp通信，难么A发出的tcp报文段不仅带有自己的序号，也包含了对B发送来的tcp报文段的
确认号。以上为例，则B发送给A的报文段中32位确认号位ISN+1026

**4位头部长度**：标示tcp头部有多少个32bit（4字节），因为4位的最大值时15（二进制1111），所以最多有15个
32bit，也就是60个字节时最大的tcp头部长度。一般也称作数据偏移，指当前包中，到第几位为止是tcp头部，
之后的是tcp数据。

**6位标志位**：
- URG：谨记指针是否有效
- ACK：标示确认号是否有效，携带ack标志的报文段被称为确认报文段
- PSH：提示接收端应用程序应该立即从tcp接受缓冲区中读走数据，为后续接收的数据让出空间
- RST：表示要求对方重建连接。带RST标志的tcp报文段也叫复位报文段
- SYN：表示建立一个连接，携带SYN的tcp报文段为同步报文段
- FIN：表示告知对方本端要关闭连接了。

**16位窗口大小**：是tcp流量控制的一个手段，这里说的窗口是指接收通告窗口，她告诉对方本端的tcp接收
缓存区还能容纳多少字节的数据，这样发送端就能控制发送数据的速度。以上为例，即B中的16位窗口大
小告诉A接下来能够发送多长的数据流（多少字节）

**16位校验和**：由发送端填充，接收端对tcp报文段执行CRC算法以校验tcp报文段在传输过程中是否损坏。
该校验包含tcp头部和数据部分。是tcp可靠传输的重要保障。

**16位紧急指针**：是一个正的偏移量。它和序号字段的值相加表示最后一个紧急数据的下一字节的序号。因此
这个字段是紧急指针相对于当前序号的偏移量。在发送紧急数据时会用到这个。

---


### 二、tcp连接建立与断开
运输连接具有三个阶段：连接建立、数据传送以及连接释放。运输连接管理就是对连接建立以及连接释放过程的管控，使得其能正常运行，达到这些目的：使通信双方能够确知对方的存在、可以允许通信双方协商一些参数（最大报文段长度、最大窗口大小等等）、能够对运输实体资源进行分配（缓存大小等）。TCP连接的建立采用客户-服务器模式：主动发起连接建立的应用进程叫做客户，被动等待连接建立的应用进程叫做服务器。

##### 连接建立阶段：
- 第一次握手：客户端的应用进程主动打开，并向客户端发出请求报文段。其首部中：SYN=1,seq=x。
- 第二次握手：服务器应用进程被动打开。若同意客户端的请求，则发回确认报文，其首部中：SYN=1,ACK=1,ack=x+1,seq=y。
- 第三次握手：客户端收到确认报文之后，通知上层应用进程连接已建立，并向服务器发出确认报文，其首部：ACK=1,ack=y+1。当服务器收到客户端的确认报文之后，也通知其上层应用进程连接已建立。

> 在这个过程中，通信双方的状态如下图，其中CLOSED：关闭状态、LISTEN：收听状态、SYN-SENT：同步已发送、SYN-RCVD：同步收到、ESTAB-LISHED：连接已建立


![tcp连接建立过程](https://img-blog.csdn.net/20161203203726485?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)


##### 连接释放阶段：
- 第一次挥手：数据传输结束以后，客户端的应用进程发出连接释放报文段，并停止发送数据，其首部：FIN=1,seq=u。

- 第二次挥手：服务器端收到连接释放报文段之后，发出确认报文，其首部：ack=u+1,seq=v。此时本次连接就进入了半关闭状态，客户端不再向服务器发送数据。而服务器端仍会继续发送。

- 第三次挥手：若服务器已经没有要向客户端发送的数据，其应用进程就通知服务器释放TCP连接。这个阶段服务器所发出的最后一个报文的首部应为：FIN=1,ACK=1,seq=w,ack=u+1。

- 第四次挥手：客户端收到连接释放报文段之后，必须发出确认：ACK=1,seq=u+1,ack=w+1。 再经过2MSL(最长报文端寿命)后，本次TCP连接真正结束，通信双方完成了他们的告别。

> 在这个过程中，通信双方的状态如下图，其中：ESTAB-LISHED：连接建立状态、FIN-WAIT-1：终止等待1状态、FIN-WAIT-2：终止等待2状态、CLOSE-WAIT：关闭等待状态、LAST-ACK：最后确认状态、TIME-WAIT：时间等待状态、CLOSED：关闭状态


![tcp连接断开过程](https://img-blog.csdn.net/20161203205925133?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQv/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/Center)

---
### 三、tcp状态总结
- **CLOSED**: 表示初始状态。

- **LISTEN**: 表示服务器端的某个SOCKET处于监听状态，可以接受连接了。

- **SYN_RCVD**: 这个状态表示接受到了SYN报文，在正常情况下，这个状态是服务器端的SOCKET在建立TCP连接时的三次握手会话过程中的一个中间状态，很短暂，基本上用netstat你是很难看到这种状态的，除非你特意写了一个客户端测试程序，故意将三次TCP握手过程中最后一个ACK报文不予发送。因此这种状态时，当收到客户端的ACK报文后，它会进入到ESTABLISHED状态。

- **SYN_SENT**: 这个状态与SYN_RCVD相对应，当客户端SOCKET执行CONNECT连接时，它首先发送SYN报文，因此也随即它会进入到了SYN_SENT状态，并等待服务端的发送三次握手中的第2个报文。SYN_SENT状态表示客户端已发送SYN报文。

- **ESTABLISHED**：表示连接已经建立了。

- **FIN_WAIT_1**: 其实FIN_WAIT_1和FIN_WAIT_2状态的真正含义都是表示等待对方的FIN报文。而这两种状态的区别是：FIN_WAIT_1状态实际上是当SOCKET在ESTABLISHED状态时，它想主动关闭连接，向对方发送了FIN报文，此时该SOCKET即进入到FIN_WAIT_1状态。而当对方回应ACK报文后，则进入到FIN_WAIT_2状态，当然在实际的正常情况下，无论对方何种情况下，都应该马上回应ACK报文，所以FIN_WAIT_1状态一般是比较难见到的，而FIN_WAIT_2状态还有时常常可以用netstat看到。

- **FIN_WAIT_2**：上面已经详细解释了这种状态，实际上FIN_WAIT_2状态下的SOCKET，表示半连接，也即有一方要求close连接，但另外还告诉对方，我暂时还有点数据需要传送给你，稍后再关闭连接。

- **TIME_WAIT**: 表示收到了对方的FIN报文，并发送出了ACK报文，就等2MSL后即可回到CLOSED可用状态了。如果FIN_WAIT_1状态下，收到了对方同时带FIN标志和ACK标志的报文时，可以直接进入到TIME_WAIT状态，而无须经过FIN_WAIT_2状态。 
- 例外状态。正常情况下，当你发送FIN报文后，按理来说是应该先收到（或同时收到）对方的ACK报文，再收到对方的FIN报文。但是CLOSING状态表示你发送FIN报文后，并没有收到对方的ACK报文，反而却也收到了对方的FIN报文。什么情况下会出现此种情况呢？其实细想一下，也不难得出结论：那就是如果双方几乎在同时close一个SOCKET的话，那么就出现了双方同时发送FIN报文的情况，也即会出现CLOSING状态，表示双方都正在关闭SOCKET连接。
- **CLOSE_WAIT**: 这种状态的含义其实是表示在等待关闭。怎么理解呢？当对方close一个SOCKET后发送FIN报文给自己，你系统毫无疑问地会回应一个ACK报文给对方，此时则进入到CLOSE_WAIT状态。接下来呢，实际上你真正需要考虑的事情是察看你是否还有数据发送给对方，如果没有的话，那么你也就可以close这个SOCKET，发送FIN报文给对方，也即关闭连接。所以你在CLOSE_WAIT状态下，需要完成的事情是等待你去关闭连接。

- **LAST_ACK**: 这个状态还是比较容易好理解的，它是被动关闭一方在发送FIN报文后，最后等待对方的ACK报文。当收到ACK报文后，也即可以进入到CLOSED可用状态了。

> 被动关闭的一方发送FIN之后，主动关闭的一方进入TIME_WATI状态，TIME_WAIT状态需要等待2MSL才进入CLOSED状态的原因是：主动关闭的一方发送ACK给被动关闭的一方，如果被动关闭的一方接收到该ACK，则被动关闭的一方进入CLOSED状态；而如果该ACK丢失，则被动关闭的一方会进行重发，这需要主动关闭的一方等待一定时间确认被动关闭的一方收到自己对FIN的ACK之后，再进入CLOSED状态。

---


### 四、tcp可靠传输原理
**名词申明**：自动重传请求（Automatic Repeat-reQuest，ARQ）

**1、停止等待协议**

这个协议说白了就是对于收到数据包后的确认机制，基本过程如下：

- 发送方A向接收方B发送数据分组M1；
- 接收方B收到分组M1后想A发送M1的确认；
- A接收到B发送的对于M1后的确认之后再发送分组M2；
- 若中间的某一个分组丢失或是延迟，则A就会使用超时计时器进行超时重传，每个分组发送时都会保留其副本并且设置超时计时器；


**这里对于超时重传机制需要三点说明：**
- A发送自己的每个分组，必须暂时保存已发送分组的一个副本，直到收到该副本的确认；
- 分组和确认分组都必须编号，这样才能对分组进行区分；
- 超时计时器设置的时间应当比数据在分组传输的平均往返时间更长一些；
  

 上面的机制保证了在不可靠的信道上时间可靠的传输，这种机制称为自动重传请求(ARQ)，即重传的请求是自动的，不需要接收方向发送方请求重传某个分组。下面的几个图来说明超时重传的机制：

![停止等待协议](http://blog.chinaunix.net/attachment/201402/17/26275986_1392618701BUnT.png)

![确认丢失和确认迟到](http://blog.chinaunix.net/attachment/201402/17/26275986_1392618712cMNm.png)

![信道利用率](https://ss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=4207667881,3103929023&fm=15&gp=0.jpg)

**信道利用率**：U = TD / (TD + RTT + TA)

**2、回退n帧ARQ协议（GBN go-back-n）**



```
发送方使用滑动窗口发送，但是接收放不维护一个对应的滑动窗口，无法缓存比如2号包丢失后，3号包的数据，
所以在2号包丢失以后，通知发送端，将2号及其之后的包全部重发。
由于停止等待ARQ协议信道利用率太低，所以需要使用连续ARQ协议来进行改善。
这个协议会连续发送一组数据包，然后再等待这些数据包的ACK。
```


发送方采用流水线传输。流水线传输就是发送方可以连续发送多个分组，不必每发完一个分组就停下来等待对方确认。如下图所示：

![image](https://img-blog.csdnimg.cn/201903070951420.png)


连续ARQ协议通常是结合滑动窗口协议来使用的，发送方需要维持一个发送窗口，如下图所示：

![收到一个确认后发送窗口向前滑动](http://blog.chinaunix.net/attachment/201402/17/26275986_1392619020316V.png)

图（a）是发送方维持的发送窗口，它的意义是：位于发送窗口内的5个分组都可以连续发送出去，而不需要等待对方的确认，这样就提高了信道利用率。 

连续ARQ协议规定，发送方每收到一个确认，就把发送窗口向前滑动一个分组的位置。例如上面的图（b），当发送方收到第一个分组的确认，就把发送窗口向前移动一个分组的位置。如果原来已经发送了前5个分组，则现在可以发送窗口内的第6个分组。 

接收方一般都是采用累积确认的方式。也就是说接收方不必对收到的分组逐个发送确认。而是在收到几个分组后，对按序到达的最后一个分组发送确认。如果收到了这个分组确认信息，则表示到这个分组为止的所有分组都已经正确接收到了。 

累积确认的优点是容易实现，即使确认丢失也不必重传。但缺点是，不能正确的向发送方反映出接收方已经正确收到的所以分组的信息。比如发送方发送了前5个分组，而中间的第3个分组丢失了，这时候接收方只能对前2个发出确认。而不知道后面3个分组的下落，因此只能把后面的3个分组都重传一次，这种机制叫Go-back-N（回退N），表示需要再退回来重传已发送过的N个分组。


**3、选择重传ARQ协议 (SR selective-report)**


```
与GBN不同点在于，它不仅仅在发送端维护了一个滑动窗口，还在服务端也维护了一个滑动窗口，用于记录由发送端窗口发送端数据。
此时，丢失哪个包便可以直接发送请求给客户端，要求重传该包
```

发送数据包的过程与连续ARQ协议相似，只是重传的数据并不是某个数据包之后的都重传，仅仅重传丢失的。

![选择重传](https://img-blog.csdn.net/201806200028220?watermark/2/text/aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L3NpbmF0XzM2NjI5Njk2/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

如上图所示
当某一段报文丢失之后, 发送端会一直收到 1001 这样的ACK, 就像是在提醒发送端 “我想要的是 1001” 

如果发送端主机连续三次收到了同样一个 “1001” 这样的应答, 就会将对应的数据 1001 - 2000 重新发送 

这个时候接收端收到了 1001 之后, 再次返回的ACK就是7001了 
因为2001 - 7000接收端其实之前就已经收到了,

被放到了接收端操作系统内核的接收缓冲区中.

**4、滑动窗口算法**

其实以上介绍的停止等待协议，连续ARQ协议（GBN），选择重传协议（SR），都是基于滑动窗口的具体实现。

- 停止等待协议是滑动窗口大小为1；
- GBN ARQ协议是在发送端维护一个大小为N的发送窗口，在接收端维护一个大小为1的窗口；
- SR ARQ协议是在发送端与接收端各自维护一个大小大于1的窗口。

---

### 五、拥塞控制

此块内容容后完善

---

### 六、问题解惑

1. 为什么TCP是三次握手，而不是两次或者四次？

   tcp的三次握手简言之是客户端向服务端发送连接请求，服务端对收到客户端的报文进行确认，客户端对服务端对确认再次确认。

   三次握手是为了防止失效的连接请求报文段突然又传送到主机B，从而产生错误。即，客户端向服务端发送连接请求A，可能因为网络问题，或者超时的问题，客户端又向服务端发送了一个请求报文B，此时客户端已经对A作出了响应，在进行数据传输。此时服务端收到了B，然后又给客户端确认报文，没有第三次连接，服务端就一个为B等待数据传输过来，这样浪费了服务端TCP资源。

2. 为什么挥手需要4次呢？

   服务器A与服务器B通讯，简称A与B，A向B发送数据。当A的业务数据传输完毕了之后，A会向B发送一个

   - 第一次挥手：数据传输结束以后，客户端的应用进程发出连接释放报文段，并停止发送数据，其首部：FIN=1,seq=u。
   - 第二次挥手：服务器端收到连接释放报文段之后，发出确认报文，其首部：ack=u+1,seq=v。此时本次连接就进入了半关闭状态，客户端不再向服务器发送数据。而服务器端仍会继续发送。
   - 第三次挥手：若服务器已经没有要向客户端发送的数据，其应用进程就通知服务器释放TCP连接。这个阶段服务器所发出的最后一个报文的首部应为：FIN=1,ACK=1,seq=w,ack=u+1。
   - 第四次挥手：客户端收到连接释放报文段之后，必须发出确认：ACK=1,seq=u+1,ack=w+1。 再经过2MSL(最长报文端寿命)后，本次TCP连接真正结束，通信双方完成了他们的告别。

   在这个过程中，每个步骤都是需要的。第四次挥手且等带2MSL后关闭A带tcp端口是必要的，因为有几率，第四次挥手的报文因网络延迟或者丢包等问题，B又重新向A发送了第三次挥手的报文，若此时A的tcp端口已经关闭了，那么B的无法关闭tcp端口，造成资源浪费。

### 七、参考文章

- [TCP Performance - The Internet Protocol Journal - Volume 3, No. 2](https://www.cisco.com/c/en/us/about/press/internet-protocol-journal/back-issues/table-contents-5/ipj-archive/article09186a00800c8417.html)
- 图片全部都是从网上copy的，如有冒犯，请及时联系 azaizai@icloud.com
