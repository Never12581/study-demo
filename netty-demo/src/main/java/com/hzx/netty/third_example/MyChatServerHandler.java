package com.hzx.netty.third_example;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.HashSet;
import java.util.Set;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 17:37 2020/1/5
 * <p>
 * 1. 连接建立时，将给所有在线连接发送消息：X主机已上线
 * 2. 客户端A，向服务器发送消息，该服务器的所有客户端都能接收到该条消息。
 * </p>
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private final static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    // Set<Channel> channelGroup = new HashSet<>();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        // 向服务端发送消息的连接
        Channel channel = ctx.channel();

        channelGroup.stream().forEach(ch -> {
            if (channel != ch) {
                ch.writeAndFlush(channel.remoteAddress() + " 发送的消息：" + msg + "\n");
            } else {
                ch.writeAndFlush("【自己】" + msg + "\n");
            }
        });
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        // 广播 ，通知其他 已建立连接
        channelGroup.writeAndFlush("【服务器】 - " + channel.remoteAddress() + "加入 \n ");
        System.out.println("【服务器】 - " + channel.remoteAddress() + "加入 \n ");
        boolean bool = channelGroup.add(channel);

        System.out.println("channelGroup size : " + channelGroup.size());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {

        Channel channel = ctx.channel();

        // netty 自动会将 该 channel 从group中 去除，故 刚行代码不必写
        channelGroup.remove(channel);

        // 广播
        channelGroup.writeAndFlush("【服务器】 - " + channel.remoteAddress() + "离开 \n ");

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush("【服务器】 - " + channel.remoteAddress() + "上线 \n ");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();

        channelGroup.writeAndFlush("【服务器】 - " + channel.remoteAddress() + "下线 \n ");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
