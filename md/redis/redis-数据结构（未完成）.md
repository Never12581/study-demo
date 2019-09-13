# Redis-数据结构

## sds字符串

### 数据结构

```c
struct sdshdr {
    // 记录 buf 数组中已使用字节的数量
    // 等于 SDS 所保存字符串的长度
    int len;
    // 记录 buf 数组中未使用字节的数量
    int free;
    // 字节数组，用于保存字符串
    char buf[];
};
```

### sds对比c字符串的优势

- 因为len记录了字符串的长度，故在获取字符串长度的时候只要获取len的值，时间复杂度为O(1)；在c字符串中，需要遍历整个字符串，时间复杂度为O(n)。

- 杜绝缓冲区溢出

  假设两个字符串紧邻s1为redis，s2为mongoDB（c字符串，以‘\0’作为结尾）

  |      | S1   |      |      |      |      |      | s2   |      |      |      |      |      |      |      |      |      |
  | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
  | ···  | 'r'  | 'e'  | 'd'  | 'i'  | 's'  | '\0' | 'm'  | 'o'  | 'n'  | 'g'  | 'o'  | 'D'  | 'B'  | '\0' | ···  |      |

  此时执行 strcat(s1," cluster");

  若用户粗心的未为其分配足够的内存，则s1的数据将溢出到s2的空间中，导致s2保存的内容意外被修改，如下表示

  |      |      |      |      |      |      |      |      |      |      |      |      |      |      |      |      |      |
  | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- | ---- |
  | ··   | 'r'  | 'e'  | 'd'  | 'i'  | 's'  | ' '  | 'c'  | 'l'  | 'u'  | 's'  | 't'  | 'e'  | 'r'  | '\0' | ···  |      |

  与c不容，sds在空间分配策略杜绝了缓冲区溢出的可能性：当sds的api需要对sds进行修改时，api会先检查sds的空间是否满足修改所需的要求，如果不满足，api会自动将sds的空间扩展至执行修改所需的大小，然后执行追加操作。sds不需要手动修改sds空间大小，也就不存在缓冲区溢出

- 减少修改字符串时内存重分配次数

  一般程序中修改字符串长度的操作并不常见，每修改一次重新分配一次内存时可接受的。但是redis作为数据库，常用于速度要求苛刻，数据频繁修改的擦火锅暖和，如果每次修改字符串都进行一次内存的分配，对性能会造成极大的影响。

  - 空间预分配

    即当sds的API对一个sds对象进行修改并需要对sds进行空间扩展时，程序不仅会为sds分配所必须的空间，还会额外分配未使用空间。

    若增长后sds长度小于1MB，则分配与len属性同样大小的空间；

    若增场后sds长度大于等于1MB，则额外分配1M空间

    通过空间预分配策略，可以有效减少连续执行字符串增长时，内存分配的次数

  - 惰性空间释放

    惰性空间释放用于优化sds字符串缩短操作：当sds的api需要缩短sds保存的字符串时，程序并不立即使用内存重分配来回收缩短后多出来的字节，而是使用free字段对其进行保存，并等待将来使用。比如free有10个字节的空间，此时追加字符串“redis”则不用重新分配内存。

    sds提供了相应的api，在有需要的时候真正释放sds未使用空间，所以不用担心惰性空间造成的内存浪费。

- 二进制安全

  c字符串的字符必须符合某种编码（如ASCII），并且出了字符串末尾以外，字符串里面不能包含空字符，否则最先被程序读入的空字符将被认为是字符串结尾。故此，c字符串只能用来保存文本，而不能保存图片，音乐等二进制数据

- 兼容c字符串

## 链表

### 数据结构

```c
typedef struct listNode {
  // 前置节点
  struct listNode *prev;
  // 后置节点
  struct listNode *next;
  // 节点的值
  void *value ;
}listNode;

typedef struct list {
  // 表头节点
  listNode *head ; 
  // 表尾节点
  listNone *tail ;
  // 链表所包含节点数量
  unsigned long len ; 
  // 节点复制函数
  void *(*dup)(void *ptr);
  // 节点释放函数
  void (*free)(void *ptr);
  // 节点值对比函数
  int (*match)(void *ptr , void *key);
}list;
```

多个listNode可以通过prev与next指针组成双端链表。

list结构为链表提供了表头节点，表尾节点，链表长度，以及三个函数。

### 特点

- 双端：链表节点带有prev与next指针，获取某个节点的前置或者后置节点的时间复杂度为O(1)
- 无环：头节点的prev与尾节点的next为null，对链表的访问以null为终点
- 带头节点与尾节点指针：对应获取头尾节点时间复杂度为O(1)
- 带链表长度计数器：获取链表长度时间复杂度为O(1)
- 多态：链表节点使用void*指针来保存节点值

## 字典（Map）

Redis的字典由哈希表作为底层实现，一个*哈希表*里面可以有多个*哈希表节点*，每个哈希表节点就保存了字典中的一个键值对。

### 数据结构

#### 哈希表

```c
typedef struct dictht{
  // 哈希表数组
  dictEntry **table ; 
  // 哈希表大小
  unsigned long size ; 
  // 哈希表大小掩码，用于计算索引值
  // 总是等于size -1 
  unsigned long sizemask ; 
  // 该哈希表已有节点数量
  unsigned long used ;
}dictht ;
```

table属性是一个数组，数组中每个元素都是执行一个dictEntry结构的指针，每个dictEntry保存着一个键值对。size属性记录了哈希表大小，也就是table数组的大小，used属性记录当前哈希表已有节点（键值对）的数量。

#### 哈希表节点

```c
typedef struct dictEntry {
  // 键
  void *key ; 
  // 值
  union{
    void *val;
    uint64_t u64;
    int64_t s64;
  }
  // 指向下一个哈希表节点，形成链表,为解决hash冲突
  struct dictEntry *next;
}dictEntry;
```

key属性保存着键值对中的键，v属性保存着键值对中的值，其中键值对中的值，可以时一个指针，或者是一个uint64_t的整数或者是int64_t的整数。

next属性时指向另一个哈希表节点的指针，用以解决哈希冲突问题。

> 哈希值冲突时，总是将当前的键值对链到表头的位置，故时间复杂度为O(1)

#### 字典

```c
typedef struct dict {
  // 类型特定函数
  dictType *type ; 
  // 私有数据
  void *privdata;
  // 哈希表
  dictht ht[2];
  // rehash索引
  // 当rehash不在进行时 为-1
  int trehashidx;
} dict ;
```

type属性和privdata属性时i 针对不同类型的键值对，为创建多态字典而设置的

- type属性时一个执行dictType结构的指针，每个dictType结构保存了一簇用于操作特定类型的键值对的函数，Redis会为用途不同的字典设置不同的类型特定函数
- privdata属性则保存了需要传给那些类型特定函数的可选参数

```c
typedef struct dictType {
  	// 计算哈希值的函数 
    uint64_t (*hashFunction)(const void *key);
    // 复制键的函数
    void *(*keyDup)(void *privdata, const void *key);
    // 复制值的函数
    void *(*valDup)(void *privdata, const void *obj);
    // 对比键的函数
    int (*keyCompare)(void *privdata, const void *key1, const void *key2);
    // 销毁键的函数
    void (*keyDestructor)(void *privdata, void *key);
    // 销毁值的函数
    void (*valDestructor)(void *privdata, void *obj);
} dictType;
```

ht属性是包含两个项的数组，每一个项都是一个dictht的哈希表，一般只用ht[0]，h[1]只会在哈希表进行rehash时使用。

rehashidx，记录了rehash进度，如果目前没有在rehash则为-1。

### 哈希算法

redis计算hash值与索引值的算法如下

- 计算哈希值：hash = dict->type->hashFunction(key)
- 计算索引值：index= hash & ht[x].sizemask

### rehash

随着操作不断执行，哈希表保存的键值对会逐渐增多或者减少，为了让哈希表的负载因子在一个合理的范围内，程序需要对哈希表进行扩容或者收缩。

扩容和收缩的过程通过rehash来实现，步骤如下：

1. 为字典的ht[1]分配空间，这个哈希表空间的大小取决于要执行的操作，以及ht[0]当前包含的键值对数量（即ht[0].used的值）
   1. 若扩展，则大小*2
   2. 若收缩，则是第一个大于等于ht[0].used的2^n
2. 将保存在ht[0]中的键值对rehash到ht[1]上，
3. 当ht[0]上的所有值都转移到ht[1]中时，将ht[1]设置为ht[0]并为ht[1]建立一个新的空白哈希表，为下一次rehash做准备

### 渐进式rehash

渐进式rehash步骤：

1. 为ht[1]分配空间，使同时存在ht[0]与ht[1]两个哈希表
2. 索引计数器变量rehashidx，将其值设置为0，表示rehash工作正式开始
3. 在rehash期间，没次对字典进行添加、删除、查找或者更新操作时，程序出了执行制定操作以外，还会顺带将ht[0]哈希表在rehashidex索引上的所有键值对rehash到ht[1]，当rehash工作完成后，程序将rehashidx属性值增一
4. 最终rehash工作完成后，将rehashidx置为-1，表示rehash已完成

渐进式rehash好处在于不需要一次rehash完成，占用过多的cpu，导致吞吐量下降。

> 在rehash的过程中，写操作会在两张表上进行，读操作会先查h[0]，后查h[1]

## 跳跃表

 跳跃表（skiplist）是一种有序数据结构，它通过在每个节点中维持多个指向其他节点的指针，从而达到快速访问节点的目的。跳跃表支持平均O(logN)，最坏O(N)复杂度的节点查找，效率可以和平衡二叉树媲美。 

### 数据结构

#### 跳跃表节点

```c
typedef struct zskiplistNode {
  // 后退指针
  struct zskiplistNode *backward ; 
  // 分值
  double score ;
  // 成员对象
  robj *obj ; 
  // 层
  struct zskiplistLevel {
    // 前进指针
    struct zskiplistNode *forward ;
    // 跨度
    unsigned int span ; 
  } level[] ;
}zskiplistNode ;
```

- 层（level）：

#### 跳跃表

```c
typedef struct zskiplist {
  // 表头节点 和表尾节点
  struct zskiplistNode *header , *tail ; 
  // 表中节点数量
  unsigned long length ; 
  // 表中层数最大的节点的层数
  int level ;
}zskiplist ;
```













