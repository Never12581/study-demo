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

