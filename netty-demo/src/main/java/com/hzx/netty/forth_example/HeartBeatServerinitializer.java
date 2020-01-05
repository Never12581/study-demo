package com.hzx.netty.forth_example;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:39 2020/1/5
 */
public class HeartBeatServerinitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ch.pipeline().addLast(new IdleStateHandler(5, 7, 10,TimeUnit.SECONDS))
                .addLast(new HeartBeatServerHandler());
    }
}
