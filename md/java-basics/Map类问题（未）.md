# Map类问题

## HashMap

### HashMap概述

hashmap底层是由数组和链表组成的。在jdk1.8中，由添加进了红黑树，在默认实现中，当一个桶内的链达到8时会转化为红黑树，当红黑树的节点数小于等于6时，将从红黑树转化回链表，是线程不安全的容器。

### HashMap扩容

#### 在jdk1.7及之前的HashMaph扩容

```java
public class HashMap <K,V>   extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable{
		/**
     * Transfers all entries from current table to newTable.
     */
    void transfer(Entry[] newTable, boolean rehash) {
        int newCapacity = newTable.length;
        for (Entry<K,V> e : table) {
            while(null != e) {
                Entry<K,V> next = e.next;
                if (rehash) {
                    e.hash = null == e.key ? 0 : hash(e.key);
                }
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            }
        }
    }
} 
```

上述代码逻辑请见下图

![https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/java-basics/jdk1.7HashMap-resize%E8%BF%87%E7%A8%8B.jpg](https://raw.githubusercontent.com/Never12581/study-demo/master/other-file/picture/java-basics/jdk1.7HashMap-resize过程.jpg)

图较为简陋，若e中的所有节点的哈希值所落到的桶都是i的话，最终resize之后结果为: e->d->c->b->a->x->y->z

#### jdk1.8扩容（以链表扩容举例）

```java
public class HashMap extends AbstractMap<K,V>
    implements Map<K,V>, Cloneable, Serializable{
    final Node<K,V>[] resize() {
    		if (oldTab != null) {
            for (int j = 0; j < oldCap; ++j) {
                Node<K,V> e;
                if ((e = oldTab[j]) != null) {
                    oldTab[j] = null;
                    if (e.next == null)
                        newTab[e.hash & (newCap - 1)] = e;
                    else if (e instanceof TreeNode)
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // preserve order
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next;
                            // 这里是对hash做了一次优化，因为数组长度是原先的两倍
                            // 所以重新计算hash值 ， 可能是与之前值相同，或者为之前值加上 老数组的长度
                            // 老数组的长度就是新增数组的长度
                            // 若与运算 计算得0 ，则在老位置
                            // 若与运算 计算不为0（一般是老数组长度），则在当前index加上老数组长度（也是新增部分数组长度）
                            if ((e.hash & oldCap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            }
                            else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);
                        if (loTail != null) {
                            loTail.next = null;
                            newTab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;
                        }
                    }
                }
            }
        }
}
```

这里比较简单，若表达式(e.hash & oldCap) == 0 成立，则证明扩容后他仍在当前桶中，不然就在当前位置加上原先数组长度的桶中。故只挑loHead与loTail举例，其实就是简单的把 loHead 赋值给newTab[j]，期间只有第一次对loHead进行赋值，后面都没有修改。

#### jdk1.7扩容时产生死循环原因

理解不了，有几个疑问：

1. 是否线程未得到时间片的时候，会将当前状态刷回主存？（应该不会，不然要volatile关键字了）
2. 在上述问题中，链表是否为特殊情况？



## HashTable

了解的不多

- put时value不能为空
- 在存在线程不安全的操作上基本加上了synchronized关键字



## ConcurrentHashMap

### ConcurrentHashMap 概述

在put时，key与value均不能为null。其他基本同HashMap，在jdk1.7与1.8中，一个采取分段锁，一个使用CAS与锁定桶中第一个元素（即链表的首元素或者树的根节点）来保证线程安全。

- jdk1.7 采用 Segment + HashEntry + Unsafe的方式进行实现
- jdk1.8 采用Synchronized + CAS + Node + Unsafe 的实现

### 问题

- ConcurrentHashMap的锁分段技术
- ConcurrentHashMap的读是否要加锁，为什么
- ConcurrentHashMap的迭代器是强一致性的迭代器还是弱一致性的迭代器

