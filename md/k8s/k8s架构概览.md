本文是k8s的架构概览，是 csdn 波波微课 视频ppt 的补充，因本人水平不够，建议有部分基础的同学浏览（若无基础，可能会因为我描述的不准确而误导）

### 概览

1. K8s 集群中有两类角色，Master 节点，Worker 节点
   - master节点 ：管理调度集群资源 （多节点构成，物理机或者虚拟机）
   - worker节点：资源提供者（多节点构成，物理机或者虚拟机）
2. worker节点 资源单位：pod
   - pod 就是 k8s 云平台提供的虚拟机
   - pod 中可包含多个应用容器
   - 一个pod 中的容器 共享 节点的网络栈和资源
3. 容器 即为 cpu 与 内存的资源隔离单位
4. k8s 是为了解决 集群资源调度问题。当有应用发布请求的时候，k8s需要根据集群资源空闲状况，经应用分配到空闲的节点上
5. k8s 时刻监控集群，如果有节点或者pod 挂了，需要重新协调并启动pod，该行为称为*自愈*
6. k8s需要管理网络 ，保证pod 服务之间可以互通互联

### master 节点构成

1. etcd ： k8s 集群 集中状态 存储 （所有集群状态数据，节点，pods，发布，配置）
   - etcd 分布式 kv数据库 ，是用 raft 分布式一致性算法
   - 可以独立部署，或与master 部署一起 
2. API Server ： 接口与通讯总线
   - 用户通过 kubectl 、 dashboard 、 sdk 操作 APIServer 与集群交互
   - 集群内部其他组件 Kubelet，Kube-Proxy ，ControllerManager，Scheduler，都是通过API Server 与集群交互
   - APIServer 可以认为是 etcd 的代理 ， 是唯一能访问，操作etcd 数据库的组件
   - 同时也是事件总线，其他组件可以注册在ApiServer ，当有事件发生，会将事件通知到关注的组件
3. Scheduler ：是 k8s 集群调度决策的组件
   - 掌握当前集群资源使用情况
4. Controller Manager：保证集群状态最终一致的组件
   - 通过Api Server 监控集群状态，确保集群实际状态与预期状态一致
   - 如：一个应用需要发布10个pods，则由Controller Manager 保证最终启动10个pods，如果某个pod 挂了， 由该组件协调重启，如果多了，协调关闭
   - k8s 自愈 实现的机制

### Worker 节点构成

1. kubelet：worker 节点资源管理者
   - 监听APIServer 事件，根据master 节点的指示 启动或关闭 pod  
   - 将本节点数据汇报给master 节点
2. Container Runtime ： worker 容器资源管理者 
   - 如果采用 docker 容器，则为 docker engine
   - Kubelet 并不直接管理节点资源，委托给 container runtime 管理资源 
   - 启动容器时，本地没有对应镜像缓存 ，则会 去docker hub 上拉取 镜像 ， 缓存在本地
3. Kube-proxy : 管理 k8s 服务网络，service 网络
   - pod 是 不固定的，比如 ip 变动 （屏蔽 pod 的ip 变动，引入了service 概念）
   - 将内部服务暴露给外网时，也需要经过 kube-proxy 进行转发

### 流程样例

以  创建一个新的 ReplicaSet 为例 

1. kubectl apply -f . 客户端发起一个新建 ReplicaSet 请求 给 API Server 。
2. API Server 将 请求写入 etcd 数据库 ，并发送 事件 给 Controller manager 
3. Controller manager 对比当前 集群 状态 与 预期集群状态 不同 ，发起创建 预期 pod 的请求 给 APIServer 
4. APIServer 收到请求后，发送事件 通知 Scheduler ， Scheduler 运行调度算法，选择空闲的node 节点，通过APIServer 更新pod 定义，将pod 指派到具体要发布的节点上
5. APIServer 通知相应的节点 通知到 kubelet 
6. kubelet 接收到 通知后， 通过 container runtime 创建容器，同时 kubelet 监控容器