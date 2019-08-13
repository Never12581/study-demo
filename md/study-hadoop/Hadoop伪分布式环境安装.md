## Hadoop伪分布式安装步骤

1. jdk安装（这不用说了吧）

2. 安装ssh

   1. sudo yum install ssh
   2. Ssh-keygen -t rsa
   3. cp ~/.ssh/id_rsa.pub ~/.ssh/authorized_keys

3. 下载并解压hadoop

   1. cdh网站下载
   2. tar -zxvf     

4. hadoop配置文件修改

   1. Hadoop_home/etc/hadoop

      1. Hadoop-env.sh   -> export JAVA_HOME = 此处配置真实JAVA_HOME地址

   2. 修改core-site.xml

      ```xml
      <property>
        <name>fs.defaultFS</name>
        <value>hdfs://hadoop000:8082</value>
      </property>
      
      <property>
      	<name>hadoop.tmp.dir</name>		<!-- 临时文件目录 -->
        <value>/home/hadoop/app/tmp</value>
      </property>
      ```

   3. 修改hdfs-site.xml

      ```xml
      <property>
      	<name>dfs.replication</name> <!-- 副本系数，因单机故修改为1 -->
        <value>1</value>
      </property>
      ```

   4. 修改slaves （单机版本不需要配置）

      DN的ip，直接写入便可

5. 启动hdfs

   1. 格式化文件系统（仅第一次执行，不可重复执行） 

      ```shell
      ./hdfs namenode -format
      ```

   2. 启动hdfs

      ```shell
      ./sbin/start-dfs.sh
      ```

   3. 验证是否启动成功

      1. jps		多出DataNode，NameNode，SecondaryNameNode三个进程
      2. 浏览器访问方式： http://ip:50070

6. 停止hdfs

   ```shell
   ./sbin/stop-dfs.sh
   ```

   

第3章 1:03:59