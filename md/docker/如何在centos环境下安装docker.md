# 如何在centos环境下安装docker

``` shell
# 下载安装
yum install -y docker 

# 启动服务
systemctl start docker.service

# 开机自动启动
sudo systemctl enable docker
```

