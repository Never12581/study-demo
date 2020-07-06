### 负一、遇到的坑

1. innodb的写锁是在 需要的时候添加，在整个事务执行结束后释放的。所以，在事务中，一种优化策略是将写 sql 往后放， 最大程度的减轻 写锁的压力。

   > 案例：同步 fio 的代码映射，至值配置中心时。数据量过大，选择多线程执行插入；又因为懒得处理垃圾数据，故选择放在事务之中。然后发现始终会出现等待写锁超时，事务回滚的现象。
   >
   > 对应解决方案：
   >
   > 1. 单线程执行，放开事务超时时间
   > 2. 在写命令处，单独新建方法，新建事务，降低写锁影响面。

### 零、数据库事务的基本介绍

事务（Transaction）是由一系列对系统中数据进行访问与更新的操作所组成的一个程序执行逻辑单元

#### ACID特性
- **原子性（Atomicity）**：事务作为一个整体被执行，包含在其中的对数据库的操作要么全部被执行，要么都不执行。
- **一致性（Consistency）**：事务应确保数据库的状态从一个一致状态转变为另一个一致状态。一致状态的含义是数据库中的数据应满足完整性约束。
- **隔离性（Isolation）**：多个事务并发执行时，一个事务的执行不应影响其他事务的执行。
- **持久性（Durability）**：已被提交的事务对数据库的修改应该永久保存在数据库中。 

#### 隔离级别
- **READ-UNCOMMITTED（读未提交）**：该级别允许用户脏读，即A事务可以读取到B事务未提交到数据
- **READ_COMMITTED（读已提交）**：无法解决可重复读的问题。事务A去读取表a中的数据，当事务A第一次读完表a中当数据时，事务B对表a中对数据进行修改，并将事务条，此时 事务A再次读取表a中数据时，两次读取结果不一致，造成不可重复读对问题
- **REPEATABLE_READ（可重复读）**：可重复读就是保证在事务处理过程中，多次读取同一个数据时，该数据的值和事务开始时刻是一致的。因此该事务级别进制了不可重复读取和脏读，但是有可能出现幻读的数据。
- **SERIALIZABLE(顺序读)**：顺序读是最严格的事务隔离级别。它要求所有的事务排队顺序执行，即事务只能一个接一个地处理，不能并发。

### 一、spring与事务

#### Spring对传播性质

- **REQUIRED**：支持当前事务，如果当前没有事务则新建一个
- **SUPPORTS**：支持当前事务，如果不存在则非事务性执行。
- **MANDATORY**：支持当前事务，如果事务不存在则报错
- **REQUIRES_NEW**：创建新事务，如果当前存在事务则将该事务挂起
- **NOT_SUPPORTED**：不创建事务，如果当前存在事务则将该事务挂起
- **NEVER**：以非事务方式执行，如果存在事务，则引发异常
- **NESTED**：如果当前存在事务，则在嵌套事务内执行。如果当前没有事务，则执行与PROPAGATION_REQUIRED类似的操作。

#### Spring 传播特性验证

##### 前置了解

1. 实验地址：https://github.com/Never12581/study-demo/tree/master/transactional-demo 

2. 查询 事务id

   ```sql
   SELECT TRX_ID FROM INFORMATION_SCHEMA.INNODB_TRX WHERE TRX_MYSQL_THREAD_ID = CONNECTION_ID( );
   ```

3. 查询 事务名

   ```java
   TransactionSynchronizationManager.getCurrentTransactionName();
   ```

4. 实验为 ServiceA methodA 调用 ServiceB methodB，以下简称A 与 B

##### REQUIRED 验证

1. A 与 B 均不加上 Transactional 注解 

   结论：均未开启事务

2. A 加上 注解 且 传播性质为 REQUIRED  ， B 不加上注解 

   结论：A 与 B 共用一个事务 

3. A 加上注解 且 传播性质为 REQUIRED  ， B  加上注解 且 传播性质为 REQUIRED

   结论：A 与 B 共用一个事务 



#### Spring事务源码分析（待续）


### 二、基于innoDB存储引擎的事务详解（待续）

