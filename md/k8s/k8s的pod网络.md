## pod 网络概念

### 第0层 Node 节点网络

保证k8s节点之间能够正常寻址和互通，由底层网络设置支持

### 第1层 pod 网络

使同一节点上的pod 与不同节点上的 pod 看起来在同一平面网络内，能通ip寻址与相互通信

pod 网络地址是由集群统一分配的，不会冲突

1. 同一节点上的pod网络
   - eth0，节点主机上的网卡，支持流量出入的设备，支持k8s节点之间做 ip 寻址与互通的设备
   - docker0：虚拟网卡，支持节点中的pod 之间的 ip 寻址和互通的设备
   - veth0：pod 内虚拟网卡，支持pod中的容器 之间的 ip 寻址和互通的设备
2. 不同节点上的pod 网络
   - 路由方案：通过路由设备为k8s 网络单独划分网段，配置路由器支持网络转发，实际例子，看波波微课的图片。即交换机捕获请求的pod ip，转发至对应的node ip上
   - 覆盖网络（overlay network）：即 pod 网络数据包，被封装为当前pod 所在node 数据包，通过交换机转发至对应的node，再解包，发送至对应的目标pod 
3. CNI 