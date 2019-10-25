# 集群

Redis集群是redis提供的分布式数据库方案，集群通过分片（sharding）来进行数据共享，并提供复制和故障转移的功能。

## 节点

一个redis集群通常由多个节点（node）组成。刚开始时，每个节点相互独立处于一个仅包含自己的一个集群中。要组建一个真正的集群，必须将各个节点连接起来，构成一个包含多个节点的集群。

连接各个节点可以使用命令：cluster meet 来完成。

> cluster meet <ip> <port>

向一个节点发送cluster meet命令时，可以让node节点与 ip和port 所制定的节点进行握手（handshake），当握手成功后，node节点会将ip和port所指定的节点添加到当前所在集群中。

### 启动节点

一个节点就是运行在集群模式下的一个redis服务器，Redis服务器再启动时会根据cluster-enabled配置选项是否为yes来决定是否开启服务器的集群模式。

```flow
st=>start: 启动服务器
cond=>condition: cluster-enabled
选项的值为yes？
yes-cond=>operation: 开启服务器的集群模式
成为一个节点
no-cond=>operation: 开启服务器单机模式
（stand alone）成为一个普通服务器

st->cond
cond(yes)->yes-cond
cond(no)->no-cond
```

节点（运行在集群模式下的Redis服务器）会继续使用所有在单机模式中使用的服务器组件，比如说：

- 节点会继续使用文件事件处理器来处理 命令请求和命令回复
- 节点会继续使用时间事件处理器来执行serverCron函数，而serverCron函数又会调用集群模式特有的clusterCron函数。clusterCron函数负责执行在集群模式下需要执行的常规操作，例如想集群中的其他节点发送Gossip消息，检查节点是否短线，或者检查是否需要对下线节点进行自动故障转移
- 节点会继续使用数据库来保存键值对数据，键值对依然会是各种不同类型的对象
- 节点会继续使用RDB持久化模块和AOF持久化模块来执行持久化工作
- 节点会继续使用发布与订阅模块来执行PUBLISH、SUBSCRIBE等命令
- 节点会继续使用复制模块来进行节点的复制工作
- 节点会继续使用Lua脚本环境来执行客户端输入的Lua脚本

除此以外，节点会继续使用redisServer结构来保存服务器的状态，使用redisClient结构来保存客户端的状态，至于那些只有在集群模式下才会用到的数据，节点将它们保存到了cluster.h/clusterNode结构、cluster.h/clusterLink结构，以及cluster.h/clusterState结构里面。

### 集群数据结构

clusterNode 结构保存了一个节点的当前状态，比如节点的创建时间、节点名字、节点当前的配置纪元、节点的ip与port等等。

每个节点都会使用一个clusterNode结构来记录自己的状态，并为集群中所有其他的节点（包括主节点和从节点）都创建一个响应的clusterNode结构，以记录其他节点的状态：

```c
typedef struct clusterNode {
    mstime_t ctime; /* Node object creation time. */
    // 节点名称，由40个十六进制字符组成 
    char name[CLUSTER_NAMELEN]; /* Node name, hex string, sha1-size */
    // 节点标识
    // 使用各种不容的标识值记录节点的角色（主节点、从节点）
    // 以及节点当前所处状态（在线、下线）
    int flags;      /* CLUSTER_NODE_... */
    // 节点当前配置纪元，用于故障转移
    uint64_t configEpoch; /* Last configEpoch observed for this node */
    unsigned char slots[CLUSTER_SLOTS/8]; /* slots handled by this node */
    int numslots;   /* Number of slots handled by this node */
    int numslaves;  /* Number of slave nodes, if this is a master */
    struct clusterNode **slaves; /* pointers to slave nodes */
    struct clusterNode *slaveof; /* pointer to the master node. Note that it
                                    may be NULL even if the node is a slave
                                    if we don't have the master node in our
                                    tables. */
    mstime_t ping_sent;      /* Unix time we sent latest ping */
    mstime_t pong_received;  /* Unix time we received the pong */
    mstime_t fail_time;      /* Unix time when FAIL flag was set */
    mstime_t voted_time;     /* Last time we voted for a slave of this master */
    mstime_t repl_offset_time;  /* Unix time we received offset for this node */
    mstime_t orphaned_time;     /* Starting time of orphaned master condition */
    long long repl_offset;      /* Last known repl offset for this node. */
    char ip[NET_IP_STR_LEN];  /* Latest known IP address of this node */
    int port;                   /* Latest known clients port of this node */
    int cport;                  /* Latest known cluster port of this node. */
    clusterLink *link;          /* TCP/IP link with this node */
    // 保存连接节点所需的有关信息
    list *fail_reports;         /* List of nodes signaling this as failing */
} clusterNode;
```

clusterNode结构的link属性时一个clusterLink结构，该结构保存了连接节点所需的有关信息，比如套接字描述符，输入缓冲区和输出缓冲区：

```c
typedef struct clusterLink {
    mstime_t ctime;             /* Link creation time */
    int fd;                     /* TCP socket file descriptor */
  	// 发送给其他节点的消息
    sds sndbuf;                 /* Packet send buffer */
    // 从其他节点接收的消息
    sds rcvbuf;                 /* Packet reception buffer */
    // 与这个连接相关联的node，没有则为null
    struct clusterNode *node;   /* Node related to this link if any, or NULL */
} clusterLink;
```

> #### redisClient结构和clusterLink结构的相同与不同之处
>
> redisClient结构和clusterLink结构都有自己的套接字描述符与输入、输出缓冲区，这两个结构的区别在于，redisClient结构中的套接字和缓冲区是用来连接客户端的，而clusterLink结构中的套接字和缓冲区则用户连接节点的

每个节点都保存着一个clusterState结构，这个结构记录了在当前节点的视角下，集群目前所处的状态，例如集群时在线还是下线，集群包含多少个节点，集群当前的配置纪元，诸如此类：

```c
typedef struct clusterState {
    // 指向当前节点的指针
    clusterNode *myself;  /* This node */
    // 集群当前纪元，用于实现故障转移
    uint64_t currentEpoch;
    // 当前集群状态，上线还是下线
    int state;            /* CLUSTER_OK, CLUSTER_FAIL, ... */
    // 集群中至少处理着一个槽点节点的数量
    int size;             /* Num of master nodes with at least one slot */
    // 集群节点名单
    // 字典的键为节点的名字，字典的值为节点对应的clusterNode结构
    dict *nodes;          /* Hash table of name -> clusterNode structures */
    dict *nodes_black_list; /* Nodes we don't re-add for a few seconds. */
    clusterNode *migrating_slots_to[CLUSTER_SLOTS];
    clusterNode *importing_slots_from[CLUSTER_SLOTS];
    clusterNode *slots[CLUSTER_SLOTS];
    uint64_t slots_keys_count[CLUSTER_SLOTS];
    rax *slots_to_keys;
    /* The following fields are used to take the slave state on elections. */
    mstime_t failover_auth_time; /* Time of previous or next election. */
    int failover_auth_count;    /* Number of votes received so far. */
    int failover_auth_sent;     /* True if we already asked for votes. */
    int failover_auth_rank;     /* This slave rank for current auth request. */
    uint64_t failover_auth_epoch; /* Epoch of the current election. */
    int cant_failover_reason;   /* Why a slave is currently not able to
                                   failover. See the CANT_FAILOVER_* macros. */
    /* Manual failover state in common. */
    mstime_t mf_end;            /* Manual failover time limit (ms unixtime).
                                   It is zero if there is no MF in progress. */
    /* Manual failover state of master. */
    clusterNode *mf_slave;      /* Slave performing the manual failover. */
    /* Manual failover state of slave. */
    long long mf_master_offset; /* Master offset the slave needs to start MF
                                   or zero if stil not received. */
    int mf_can_start;           /* If non-zero signal that the manual failover
                                   can start requesting masters vote. */
    /* The followign fields are used by masters to take state on elections. */
    uint64_t lastVoteEpoch;     /* Epoch of the last vote granted. */
    int todo_before_sleep; /* Things to do in clusterBeforeSleep(). */
    /* Messages received and sent by type. */
    long long stats_bus_messages_sent[CLUSTERMSG_TYPE_COUNT];
    long long stats_bus_messages_received[CLUSTERMSG_TYPE_COUNT];
    long long stats_pfail_nodes;    /* Number of nodes in PFAIL status,
                                       excluding nodes without address. */
} clusterState;

```

以下展示以7000节点创建的clusterState结构，这个结构从节点7000点角度记录了集群以及集群包含的三个节点的当前状态（省略部分属性）：

- 结构的currentEpoch属性为0，表示集群当前的配置纪元为0
- 结构的size属性的值为0 ，表示集群目前没有任何节点在处理槽，因此结构的state属性的值为REDIS_CLUSTER_FALL，这表示集群目前处于下线状态
- 结构的nodes字典记录了集群目前包含的三个节点，这三个节点分别由三个clusterNode结构来表示，
- 三个节点的clusterNode结构的flags属性都是REDIS_NODE_MASTER，说明三个节点都是主节点

### CLUSTER MEET命令实现

通过向节点A发送cluster meet 命令，客户端可以让接受命令等节点A将另一个节点B添加到A当前所在的集群里面：

```shell
 cluster meet <ip> <port>
```

收到命令的节点A将于节点B进行握手（handshake），以此来确认彼此的存在，并为将来的进一步通信打好基础：

1. 节点A会为节点B创建一个clusterNode结构，并将该结构添加到自己的clusterState.node字典中。
2. 之后，节点A根据cluster meet命令给定的ip与port，向节点B发送一条meet消息（message）。
3. 如果一切顺利，节点B将接收到节点A发送的meet消息，节点B会为节点A创建一个clusterNode结构，并将该结构添加到自己的clusterState.node字典中。
4. 之后，节点B向节点A返回一条pong消息。
5. 如果一切顺利，节点A将接收到节点B返回到pong消息，通过这条pong消息节点A可以知道节点B已经成功地接收到了自己发送到meet消息。
6. 之后，节点A将向节点B发送一条PING消息
7. 如果一切顺利，节点B将接收到节点A返回到ping消息，通过这条ping消息节点B可以知道节点A已经成功地收到了自己返回到pong消息，握手完成。

![节点握手过程](https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/redis-节点握手过程.jpg)

之后，节点A会将节点B的信息通过<font color='red'>Gossip协议</font>传播给集群中的其他节点，让其他节点也与节点B进行握手，最终，经过一段时间之后，节点B会被集群中所有节点认识。

## 槽指派

redis集群通过分片的方式来保存数据库中的键值对：集群的整个数据库被氛围16384个槽（slot），数据库中每个键都属于这16384个槽中的一个，集群中的每个节点可以处理0个或最多16384个槽

当数据库中16384个槽都有节点在处理时，集群处于上线状态（OK）；若存在一个槽没有得到处理，则集群处于下线状态（fail）。

通过节点发送 cluster addslots 命令，我们可以将一个或多个槽指派（assign）给节点负责：

```shell
127.0.0.1:7000> cluster addslots 0 1 2 3 4 ... 7
```

同理，可以将剩余的槽分配给其他节点。

将所有的槽分配给其他节点的时候，集群处于上线状态，可以使用以下命令查看：

```shell
# 查看集群状态
127.0.0.1:7000> cluster info
cluster_state:ok
cluster_slots_assigned:16384
cluster_slots_ok:16384
cluster_slots_pfail:0
cluster_slots_fail:0
cluster_known_nodes:3
cluster_size:3
cluster_current_epoch:0
cluster_stats_messages_sent:2699
cluster_stats_messages_received:2617

# 查看节点信息
127.0.0.1:7000> cluster nodes 
```

### 记录节点的槽指派信息

clusterNode 结构的slots属性和numslots属性记录了节点负责处理哪些槽：

```c
struct clusterNode {
  // ...
  unsigned char slots[16384/8];
  
  int numslots;
  // ...
};
```

slots 是一个二进制位数组（bit array) ，这个数组的长度为16384/8 = 2048个字节，共包含了16384个二进制位。

Redis以0为索引，16383为终止索引，对slots数组中的16384个二进制位进行编号，并根据索引i上的二进制位的值来判断节点是否负责处理槽i。

![https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/slots%E6%95%B0%E7%BB%84%E7%A4%BA%E4%BE%8B.png](https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/slots数组示例.png)

上图展示了，当前节点仅处理0～7号槽。

因为取出和设置slots数组中任意一个二进制位的值的时间复杂度位O(1)，所以对于一个给定节点的slots数组来说，程序检查节点是否负责处理某个槽，又或者将某个槽指派给节点负责，这两个动作的时间复杂度都是O(1)。

### 传播节点的槽指派信息

 一个节点除了会将自己负责处理的槽记录在clusterNode结构的slots属性和numslots属性外，它还会将自己的slots数组通过消息发送给集群中其他节点，以此来告知其他节点自己目前负责处理哪些槽。

因此集群中的每个节点都知道数据库中的16384个槽分别被指派给了集群中的哪些节点

### 记录集群所有槽点指派信息

clusterState结构中的slots数组记录了集群中所有16384哥槽点指派信息：

```c
typedef struct clusterState{
  // ...
  clusterNode *slots[16384]
  // ...
}clusterState;
```

slots数组包含了16384个项，每个数组项都是一个指向clusterNode 结构的指针：

- 如果 slots[i] 指针指向null，那么表示槽i尚未指派给任何节点
- 如果 slots[i] 指针指向一个clusterNode结构，那么表示槽 i 已经指派给了clusterNode结构所代表的节点

以上为例，slots[0] ~ slots[7] 指向 上图图示中 clusterNode ，而其他的数组各自指向对应的 clusterNode 。

如果只将槽指派信息保存在各个节点的clusterNode.slots数组里，会出现一些无法高效地解决的问题，而clusterState.slots 数组的存在解决了这些问题：

- 如果节点只使用 clusterNode.slots 数组 来记录槽的指派信息，那么为了知道槽 i 是否已经被指派，或者槽 i 被指派给了哪个节点，程序需要遍历clusterState.nodes 字典中的所有 clusterNode 结构，检查这些结构的slots 数组，知道找到负责处理 槽 i 的节点位置，整个过程的时间复杂度是 O(n) ，其中n为clusterState.nodes 字典保存的 clusterNode 结构的数量。
- 而通过将所有槽点指派信息保存在clusterState.slots 数组里面，程序要检查槽 i 是否已经被指派，又或者取得负责处理槽 i 的节点，只需要访问 clusterState.slots[i] 的值即可，时间复杂度为O(1)。

虽然 clusterState.slots 数组记录了集群中所有槽点指派信息，但使用 clusterNode 结构的 slots 数组来记录单个节点的槽指派信息仍然是必要的：

- 因为当程序的要将 某个节点 的槽指派信息通过消息发送给其他节点时，程序只需要将相应节点的 clusterNode.slots 数组整个发送出去就可以了。
- 另一方面，如果redis 不是用 clusterNode.slots 数组，而单独使用 clusterState.slots 数组的话，那么每次要将节点A 的槽指派信息传播给其他节点时 ， 程序必须先遍历整个clusterState.slots 数组，记录节点A 负责处理哪些槽，然后才能发送节点A 的槽指派信息。

clusterState.slots 记录了集群中所有槽点指派信息，而 clusterNode.slots 数组只记录了 clusterNode 结构所代表的槽指派信息。

### cluster addslots 命令的实现

cluster addslots 命令接受一个或多个槽作为参数，并将所有输入的槽指派给接收该命令的节点负责：

```shell
cluster addslots <slots> [slot ...]
```

该命令的实现可由以下源码表示：

```c
def CLUSTER_ADDSLOTS(*all_input_slots):
	// 遍历输入的所有槽
	for i in all_input_slots :
			// 如果有槽已经被指派，则向客户端返回错误，并终止命令执行
			if clusterState.slots[i] != null:
					reply_error()
          return 
            
  // 若均未指派，则将这些槽指派给当前节点
  for i in all_input_slots :
		// 设置clusterState结构的slots数组
		// 将slots[i] 的指针指向代表当前节点的 clusterNode 结构
		clusterState.slots[i] = clusterState.myself
		
		// 访问代表当前节点的clusterNode结构的slots数组
		// 将数组在索引i上的二进制位设置为1
		setSlotsBit(clusterState.myself.slot,i)
             
```

最后，当该命令执行完毕后，节点会通过发送消息告知集群中的其他节点，自己目前正在负责处理的槽。

## 在集群中执行命令

当客户端向节点发送与数据库键有关的命令时，接收命令的节点回计算出命令要处理的数据库键属于哪个槽，并检查这个槽是否指派给了自己：

- 如果键所在的槽正好就指派给了当前节点，那么节点直接执行命令
- 如果键所在的槽并没有指派给当前节点，那么节点会向客户端返回一个MOVED错误，指引客户端转向（redirect）至正确的节点，并再次发送之前想要执行的命令。

```flow
st=>start: 开始
e=>end: 
client_sent=>operation: 客户端向节点发送
数据库键命令
calcu_slots=>operation: 计算键属于哪个槽
responsilble_slots=>condition: 当前节点就是负责
处理键所在槽节点？
execute_order=>subroutine: 节点执行命令
return_moved=>operation: 节点向客户端
返回一个moved错误
moved_to_right_slot=>operation: 客户端根据moved错误
提供的信息 转向
至正确的节点

st->client_sent->calcu_slots->responsilble_slots
responsilble_slots(yes)->execute_order
responsilble_slots(no)->return_moved(right)->moved_to_right_slot->client_sent
responsilble_slots->

```

### 计算键属于哪个槽

节点使用以下算法来计算给定键key属于哪个槽：

```c
def slot_number(key):
	return CRC16(key) & 16383
```

其中 CRC16(key) 语句用于计算键key的CRC-16校验和，而 & 16383 语句则用于取模计算出 一个 介于 0～16383 之间的槽号。

可以使用以下命令来查看给定键属于的槽：

```shell
127.0.0.1:7000 > cluster keyslot "msg"
```

该命令也是由以上算法实现的，以下为伪码实现：

```c
def cluster_keyslot(key) : 
	# 计算槽号
	slot = slot_number(key)
  # 将槽号返回客户端
  reply_client(slot)
```

### 判断槽是否由当前节点负责

当节点计算出键所属槽 i 之后，节点就会检查自己在clusterState.slots 数组中的项 i ，判断键所在的槽是否由自己负责：

1. 如果clusterState.slots[i] 等于 clusterState.myself ，那么说明槽 i 由当前节点负责，节点可以执行客户端发送的命令
2. 如果 clusterState.slots[i] 不等于 clusterState.myself ，那么说明槽 i 并非由当前节点负责，阶段会根据 clusterState.slots[i] 指向的 clusterNode 结构所记录的节点 IP 与 port ， 会向客户端返回 moved 错误，指引客户端转向至正在处理槽 i 的节点。

### MOVED 错误

当客户端发现键所在的槽并非由自己处理时，节点就会向客户端返回一个 MOVED 错误，指引客户端转向至正在负责槽的节点。

MOVED错的的格式为：

```shell
MOVED <slot> <ip>:<port>
```

当客户端接收到节点返回的moved错误时，客户端会根据moved错误中提供的IP地址和端口号，转向至负责处理槽slot节点，并向该节点重新发送之前想要执行的命令。

![https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/redis-moved%E9%94%99%E8%AF%AF%E6%BC%94%E7%A4%BA.png](https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/redis-moved错误演示.png)

一个集群客户端通常会与集群中的多个节点创建套接字连接，而所谓的节点转向实际上就是换一个套接字发送命令。

如果客户端尚未与想要转向的节点创建套接字连接，那么客户端会先根据MOVED错误提供的IP地址和端口号来连接节点，然后再进行转向。

> #### 被隐藏的MOVED错误
>
> 集群模式的 redis-clie 客户端在接收到MOVED错误时，并不会打印出MOVED错误，而是根据MOVED错误自动进行节点转向，并打印出转向信息，所以我们时看不见节点返回的MOVED错误的：
>
> ```shell
> $ redis-cli -c -p 7000 # 集群模式
> 127.0.0.1:7000> set msg "happy new year!"
> -> Redirected to slot [6275] located at 127.0.0.1:7001
> 
> 127.0.0.1:7001> 
> ```
>
> 但是，如果我们使用单机（stand alone）模式d reds-cli客户端，再次向节点7000 发送相同的命令，那么MOVED 错误就会被客户端打印出来：
>
> ```shell
> $ redis-cli -p 7000 # 单机模式
> 127.0.0.1:7000> set msg "happy new year!"
> (error) MOVED 6257 127.0.0.1:7001
> 
> 127.0.0.1:7000>
> ```
>
> 这是因为单机模式的redis-cli客户端不清楚 moved 错误的作用，所以只会直接转向，而不会自动转向

###  节点数据库的实现

集群节点保存键值对以及键值对过期时间的方式，与单机redis服务器保存键值对以及过期方式完全相同。

节点与单机服务器在数据库方面的一个区别是，节点只能使用0号数据库，而单机服务器没有这个限制。

另外，出了将键值对保存在数据库里之外，节点还会用clusterState 结构中的 slots_to_keys 跳跃表来保存槽和键之间的关系

```c
typedef struct clusterState{
  // ...
  zskiplist *slots_to_keys;
  // ...
}clusterState;
```

slots_to_keys **跳跃表**每个节点的分值（score）都是一个槽号，而每个节点的成员（member）都是一个数据库键：

- 每当节点往数据库中添加一个新的键值对时，节点就会将这个键以及这个键所在的槽关联到slots_to_keys中
- 当节点删除数据库中的某个键值对时，节点就会在slots_to_keys 中解除关联

## 重新分片

Redis 集群的重新分片操作可以将任意数量已经指派给某个节点（源节点）的槽改为指派给另一个节点（目标节点），并且相关槽所属的键值对也会从源节点被移动到目标节点。

重新分片操作可以在线（online）操作，在重新分片的过程中，集群不需要下线，并且源节点和目标节点都可以继续处理命令请求。

### 重新分片实现原理

redis集群的重新分片操作是由redis的集群管理软件redis-trib负责执行的，redis提供了进行重新分片所需的所有命令，而redis-trib则通过向源节点与目标节点发送命令来进行重新分片的操作

Redis-trib 对集群中单个槽slot进行重新分片的步骤如下：

1. Redis-trib 对目标节点发送 cluster setslot <slot> importing <source_id> 命令，让目标节点准备好从源节点倒入（import）属于槽 slot 的键值对。
2. redis-trib 对源节点发送 cluster setslot <slot> migrating <target_id> 命令，让源节点准备好将属于槽slot的键值对迁移（migrate）至目标节点。
3. redis-trib 向源节点发送 cluster getkeysinslot <slot> <count> 命令，获得最多count个 属于 槽slot的键值对的键名（key name）。
4. 对于步骤3获得的每个键名，redis-trib都想源节点发送一个 migrate <target_id> <target_port> <key_name> 0 <timeout> 命令，将被选中的键值对从源节点迁移至目标节点
5. 重复执行步骤3与步骤4，知道源节点保存的所有属于槽slot的键值对都被迁移至目标节点为止。迁移过程如下图所示
6. redis-trib 向集群中的任意一个节点发送cluster setslot <slot> NODE <targe_id> 命令，将槽 slot 指派给目标节点，这一指派信息会通过消息发送至整个集群，最终集群中的所有节点都会知道槽slot 已经指派给了目标节点。

![https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/redis-%E9%94%AE%E8%BF%81%E7%A7%BB%E8%BF%87%E7%A8%8B.jpg](https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/redis/redis-键迁移过程.jpg)

如果重新分片涉及到了多个槽，那么redis-trib将对每个给定的槽分别执行上面给出的步骤。流程图如下：

```flow
st=>start: 开始
e=>end: 结束

refragment=>operation: 开始对槽slot
进行重新分片
target_ready=>operation: 目标节点准备导入
槽slot的键值对
source_ready=>operation: 源节点准备迁移
槽slot的键值对

source_exist_keys=>condition: 源节点是否保存了
槽slot的键

key_move=>operation: 将这些键全部
迁移至目标节点

slot_move=>operation: 将槽slot指派给
目标节点

over=>operation: 完成对槽slot的重新分片





st->refragment(left)->target_ready->source_ready(right)->source_exist_keys
source_exist_keys(no)->slot_move
source_exist_keys(yes)->key_move->slot_move
slot_move(left)->over->e
```



## ASK错误

## 复制与故障转移

## 消息

## 回顾

- 节点通过握手来将其他节点添加到自己所处的集群当中
- 集群中的16384个槽可以分别指派给集群中的各个节点，每个节点都会记录哪些槽指派给了自己，而哪些槽又被指派给了其他节点
- 节点在接到一个命令请求时，会先检查这个命令请求要处理的槽是否由自己负责，如果不是的话，节点将向客户端返回一个moved错误，moved错误携带的信息可以指引客户端专向至正在负责相关槽点节点
- 对Redis集群的重新分片工作是由redis-trib负责执行的，重新分片的关键是将属于某个槽所有的键值对从一个节点转至另一个节点。
- 如果节点A正在迁移槽i至节点B，那么当节点A没能在自己的数据库中找到命令指定的数据库键时，节点A会向客户端放回一个ASK错误，指引客户端到节点B继续查找指定的数据库键
- MOVED错误表示槽点负责权已经从一个节点转移到另一个节点，而ASK错误只是两个节点在迁移槽的过程中使用的一种临时措施
- 集群里的从节点用于复制主节点，并在主节点下线时，代替主节点继续处理命令请求
- 集群中的节点通过发送和接收消息来进行通信，常见的消息包括MEET、PING、PONG、PUBLISH、FALL五种



