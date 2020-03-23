package com.tony.edu.netty.netty;

import java.util.ArrayList;
import java.util.List;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

// 编解码一定是根据协议~ 你们定义的规范
// 网络传递的底层数据 ---> 解析为协议所对应 -- 》 java对象 。 过程就是解码decode
public class ProtcolHandller extends ChannelInboundHandlerAdapter {
    static final int PACKET_SIZE = 220;

    // 用来临时保留没有处理过的请求报文
    ByteBuf tempMsg = Unpooled.buffer();

    // in输入   --- 处理  --- out 输出
    // 收到的数据要么是 太多（粘包） 要么太少（拆包）
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // TODO 1. 解析出一个完整的正常的请求数据，交给后面的handler处理

        ByteBuf in = (ByteBuf) msg;
        List<Object> out = new ArrayList<>();
        decode(ctx,in, out); // TODO 解析
        // TODO 交给下一个处理器处理
        for (Object o : out) {
            ctx.fireChannelRead(o);
        }

    }

    private void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        System.out.println(Thread.currentThread() + "收到了一次数据包，长度是：" + in.readableBytes());
        // in 请求的数据
        // out 将粘在一起的报文拆分后的结果保留起来

        // 1、 合并报文
        ByteBuf message = null;
        int tmpMsgSize = tempMsg.readableBytes();
        // 如果暂存有上一次余下的请求报文，则合并
        if (tmpMsgSize > 0) {
            message = Unpooled.buffer();
            message.writeBytes(tempMsg);
            message.writeBytes(in);
            System.out.println("合并：上一数据包余下的长度为：" + tmpMsgSize + ",合并后长度为:" + message.readableBytes());
        } else {
            message = in;
        }

        // 2、 拆分报文
        // 这个场景下，一个请求固定长度为220，可以根据长度来拆分
        // 不固定长度，需要应用层协议来约定 如何计算长度
        // 在应用层中，根据单个报文的长度及特殊标记，来将报文进行拆分或合并
        // dubbo rpc协议 = header(16) + body(不固定)
        // header最后四个字节来标识body
        // 长度 = 16 + body长度
        // 0xda, 0xbb 魔数


        int size = message.readableBytes();
        int counter = size / PACKET_SIZE;
        for (int i = 0; i < counter; i++) {
            byte[] request = new byte[PACKET_SIZE];
            // 每次从总的消息中读取220个字节的数据
            message.readBytes(request);

            // 将拆分后的结果放入out列表中，交由后面的业务逻辑去处理
            out.add(Unpooled.copiedBuffer(request));
        }

        // 3、多余的报文存起来
        size = message.readableBytes();
        if (size != 0) {
            System.out.println("多余的数据长度：" + size);
            // 剩下来的数据放到tempMsg暂存
            tempMsg.clear();
            tempMsg.writeBytes(message.readBytes(size));
        }
    }
}
