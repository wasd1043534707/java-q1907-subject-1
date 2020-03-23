package com.tony.edu.netty.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 后续处理handdler
 */
public class PrintHandller extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 输出 bytebuf -- 收到一条完整的消息，不能够是被拆包或者粘包
        ByteBuf buf = (ByteBuf) msg;
        byte[] content = new byte[buf.readableBytes()];
        buf.readBytes(content);
        System.out.println(Thread.currentThread() + ": 最终打印" + new String(content));
        // 解析一条消息 ，找到 userID, 消息内容...
    }

    // 异常
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

}
