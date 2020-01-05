package com.hzx.netty.forth_example;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * @Author: bocai.huang
 * @Descripition:
 * @Date: Create in 20:40 2020/1/5
 */
public class HeartBeatServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;

            String eventType = null;

            switch (event.state()) {
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case ALL_IDLE:
                    eventType = "写空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "读写空闲";
                default:
            }

            System.out.println(ctx.channel().remoteAddress() + "  超时事件：" + eventType);
            ctx.close();
        }
    }
}
