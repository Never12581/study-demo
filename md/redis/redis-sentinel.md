### sentinel领头羊选举

当一个主服务器被判断为客观下线时，监视这个下线主服务器的各个sentinel 会进行商议，选举出一个领头的sentinel，并有领头sentinel对下线主服务器执行故障转移的操作。

#### 选举领头sentinel的规则与方法

- 所有在线的sentinel都有被选为领头sentinel的资格，即监视同一个主服务器的在线sentinel均有成为领头sentinel的资格

- 每次进行领头sentinel选举之后，不论成功与否，所有sentinel的配置纪元（configuration epoch）的值都会自增一次。配置纪元实际就是一个计数器，来判断当前数据是否时最新一次选举的数据

- 每个发现主服务器进入客观下线状态的sentinel都会要求其他sentinel将自己设置为局部领头sentinel

- 当一个sentinel（源sentinel）想另一个sentinel（目标sentinel）发送

  ```shell
  sentinel is-master-down-by-addr <ip> <port> <current_epoch> <runid>
  ```

  并且命令中的runid参数不是 * 而是源sentinel的runid时，这表示源sentinel要求目标sentinel将前者设置为后者的局部领头sentinel

- 设置局部领头sentinel的规则时先到先得：最先向目标sentinel发送设置要求的源sentinel将成为目标sentinel的局部领头sentinel，而之后接收到的所有设置要求都会被目标sentinel拒绝

- 目标sentinel在接收到上述命令后，将向源sentinel发送一命令回复，恢复中的leader_runid参数的值和源sentinel的运行id一致，那么表示目标sentinel将源sentinel设置成为了局部领头sentinel，之后接收到的所有设置要求都会被拒绝

- 目标sentinel在接收到 sentinel is-master-down-by-addr 命令之后，将向源sentinel返回一条命令回复，回复中leader_runid参数和leader_epoch参数分别记录了目标sentinel的局部领头sentinel的runid和配置纪元

- 源sentinel在接收到目标sentinel返回的命令回复之后，会检查回复中leader_epoch参数的值与自己的配置纪元的值是否相同，如果相同的话则检查leader_runid参数的值与源sentinel的runid相同的话，那么表示目标sentinel将源sentinel设置成了局部领头sentinel

- 如果有某个sentinel被半数以上的sentinel设置成了局部领头sentinel，那么这个sentinel成为了领头sentinel。

  > e.g. 由5个sentinel组成的sentinel系统，只要大于等于 5/2+1 = 3 ，即局部领头sentinel可以成为领头sentinel

- 因为领头sentinel需要半数以上sentinel支持，并且每个sentinel在一个配置纪元里只能设置一次局部领头sentinel，所以在一个配置纪元里只会出现一个领头sentinel

- 如果在给定的时限中，没有一个sentinel被选举为领头sentinel<font color='red'>(不论选举成功与否，每个sentinel的配置纪元参数+1)</font>，那么sentinel将会在一段时间后再次进行选举，知道选出领头sentinel为止。

#### 