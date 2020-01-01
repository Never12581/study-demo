package com.hzx.day01;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Date;

/**
 * 一、使用NIO完成网络通信的三个核心：
 * 1. 通道（channel）：负责连接
 * java.nio.channels.Channel 接口：
 * |-- SelectableChannle
 * |--SocketChannel
 * |--ServerSocketChannel
 * |--DatagramChannel
 * |--Pipe.SinkChannel
 * |--Pipe.SourceChannel
 * 2. 缓冲区（Buffer）：负责数据存取
 * 3. 选择器（selector）：是 SeletableChannel 的多路复用器，用于监控 SelectableChannel 的IO 状况
 */
public class TestNonBlockingNIO {

    @Test
    public void client() throws IOException {
        // 1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        // 2. 切换为非阻塞模式
        sChannel.configureBlocking(false);

        // 3. 分配指定大小缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 4. 发送shujugeifuwuduan
        buf.put(new Date().toString().getBytes());
        buf.flip();
        sChannel.write(buf);
        buf.clear();

        // 5. 关闭通道
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        // 1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        // 2. 切换非阻塞模式
        ssChannel.configureBlocking(false);

        // 3. 绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        // 4. 获取选择器
        Selector selector= Selector.open();

        // 5. 将通道注册到选择器上
        ssChannel.register(selector,);

    }

}
