package com.hzx.day01;

import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

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
public class TestBlockingNIO2 {

    @Test
    public void copyTest() throws IOException {

        long start = System.currentTimeMillis();
        for(int i = 0 ; i < 10 ; i++) {
            String fileName = "/Users/huangbocai/study-data/MySQL实战45讲.zip";

            FileChannel inChannel = FileChannel
                .open(Paths.get(fileName), StandardOpenOption.READ, StandardOpenOption.WRITE);

            MappedByteBuffer mappedByteBuffer = inChannel.map(MapMode.READ_WRITE, 0, inChannel.size());

            FileChannel outChannel = FileChannel.open(Paths.get(fileName + "xxxx"+i), StandardOpenOption.CREATE,
                StandardOpenOption.WRITE);

            outChannel.write(mappedByteBuffer);

            mappedByteBuffer.clear();
            outChannel.close();
            inChannel.close();
        }

        System.out.println("cost time ：" + String.valueOf(System.currentTimeMillis() - start));

    }

    @Test
    public void client() throws IOException {
        // 1. 获取通道
        SocketChannel sChannel = SocketChannel.open(new InetSocketAddress("127.0.0.1", 9898));

        FileChannel fileChannel = FileChannel.open(Paths.get("NIO.md"), StandardOpenOption.READ);

        // 2. 分配制定大小的缓中区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 3. 读取本地文件，并发送至服务器
        while (fileChannel.read(buf) != -1) {
            buf.flip();
            sChannel.write(buf);
            buf.clear();
        }

        // 4. 接收服务器的反馈
        int len = 0 ;
        while ((len = sChannel.read(buf))!=-1) {
            buf.flip();
            System.out.println( new String(buf.array(),0,len));
            buf.clear();
        }

        // 5. 关闭通道
        fileChannel.close();
        sChannel.close();
    }

    @Test
    public void server() throws IOException {
        // 1. 获取通道
        ServerSocketChannel ssChannel = ServerSocketChannel.open();

        FileChannel outChannel =
                FileChannel.open(Paths.get("NIO-2.md"), StandardOpenOption.WRITE, StandardOpenOption.CREATE);

        // 2. 绑定连接
        ssChannel.bind(new InetSocketAddress(9898));

        // 3. 获取客户端连接的通道
        SocketChannel sChannel = ssChannel.accept();

        // 4. 分配指定大小的缓冲区
        ByteBuffer buf = ByteBuffer.allocate(1024);

        // 5. 接收客户端的数据，并保存到本地
        while ((sChannel.read(buf)) != -1) {
            buf.flip();
            outChannel.write(buf);
            buf.clear();
        }

        // 6. 数据反馈
        buf.put("服务端接收服务".getBytes());
        buf.flip();
        sChannel.write(buf);

        // 6. 关闭通道
        sChannel.close();
        outChannel.close();
        ssChannel.close();

    }

}
