# 安装go环境

## 下载安装包

wget https://dl.google.com/go/go1.11.2.linux-amd64.tar.gz

## 解压至指定文件夹

tar -C /usr/local -xzf go1.11.2.linux-amd64.tar.gz

## 创建工作目录

mkdir -p /home/go/src /home/go/pkg /home/go/bin

### 配置环境变量

```shell
vim /etc/profile

export GOROOT=/usr/local/go
export GOPATH=/home/gopath
export PATH=$PATH:$GOROOT/bin:$GOPATH/bin
```

## 生效环境变量

```shell
source /etc/profile
```

## 检查安装是否成功

```shell
go env
```

