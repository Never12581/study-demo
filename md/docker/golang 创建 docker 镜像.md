# 创建golang的docker镜像

0. 学习docker 与 golang的相关知识，本处不赘述

1. 编写一个golang的测试用例main.go，如下：

   ```GO
   package main
   
   import (
       "github.com/gin-gonic/gin"
   )
   
   func main() {
   
       r := gin.Default()
       r.GET("/hello", hello)
       r.Run(":8080")
   }
   
   func hello(c *gin.Context) {
       c.JSON(200, gin.H{
          "message": "pong",
       })
   }
   ```

2. 在同级目录下创建对应Dockerfile

   ```shell
   # 使用最新版 golang 作为基础镜像
   FROM golang:latest AS builder
   #设置工作目录，没有则自动新建
   WORKDIR /go/src/
   #拷贝代码到当前
   COPY main.go /go/src/
   ## 编译
   RUN go bulild main.go
   #运行服务
   CMD ["./main"]
   ```

3. 创建镜像docker build -t my-first . 

4. 不过会失败，因为 在 go build 的过程中，在本镜像中无法获取对应的 github.com/gin-gonic/gin 依赖

5. 所以我们采取以下的方案

   ```shell
   # 使用最新版 golang 作为基础镜像
   FROM golang:latest AS builder
   #设置工作目录，没有则自动新建
   WORKDIR /go/src/
   #拷贝代码到当前
   COPY main.go .
   
   # 获取依赖
   RUN go get -d -v ./...
   RUN go install -v ./...
   
   ## 编译
   RUN go bulild main.go
   #运行服务
   CMD ["./main"]
   ```

6. 不大大概率还是失败。。。因为有些东西会被墙住。。。

7. 所以我们要采取粗暴的方式！将依赖直接打包进docker镜像中。

   ```shell
   # 使用最新版 golang 作为基础镜像
   FROM golang:latest AS builder
   #设置工作目录，没有则自动新建
   WORKDIR /go/src/
   #拷贝代码到当前
   COPY main.go /go/src/
   COPY vendor/ /go/src/
   ## 编译
   RUN go bulild main.go
   #运行服务
   CMD ["./main"]
   ```

8. 本Dockerfile中，有一个上文没提到过的vendor，其实该文件夹是存放的是当前服务的依赖。可以通过使用 go mod init，并且打开 Vendoring mode ，此时sync 你的模块时，会在当前目录下生成一个vendor文件夹

9. 当然上述的打包出来的docker 镜像太过于巨大了，可以减小镜像体积，最终，生成的Dockerfile 如下：

   ```shell
   # 使用最新版 golang 作为基础镜像
   FROM golang:latest AS builder
   #设置工作目录，没有则自动新建
   WORKDIR /go/src/
   #拷贝代码到当前
   COPY main.go /go/src/
   COPY vendor/ /go/src/
   ## 编译
   RUN CGO_ENABLED=0 GOOS=linux go build -a -installsuffix cgo -o main .
   
   
   ##基于builder构建go_server
   FROM alpine:latest
   RUN apk --no-cache add ca-certificates
   WORKDIR /root/
   COPY --from=builder /go/src/ .
   #运行服务
   CMD ["./main"]
   ```