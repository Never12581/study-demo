## HDFS架构

1. Master（NameNode/NN）带 N个 Slaves（DataNode/DN）

   1个文件会被拆分为多个block与blocksize相关

   假设blocksize=128M，一个文件130M===>2blokc ： 128M与2M

2. NN：

   1. 负责客户端请求对响应
   2. 负责元数据（文件的名称，副本系数，Block存放的DN）的管理

3. DN：

   1. 存储用户的文件对应的数据块（block）
   2. 定期向NN发送心跳信息，回报本身及其所有block信息，健康状况

4. NameNode + N个DataNode

   建议：NN和DN部署在不同节点上

5. replication factor： 副本系数，副本因子

