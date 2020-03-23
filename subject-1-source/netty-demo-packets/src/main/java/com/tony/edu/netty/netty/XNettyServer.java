package com.tony.edu.netty.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LoggingHandler;

public class XNettyServer {
    public static void main(String[] args) throws Exception {

        // 1、 线程定义
        // accept 处理连接的线程池
        EventLoopGroup acceptGroup = new NioEventLoopGroup();
        // read io 处理数据的线程池 --- 同一时间处理的请求数量（数据读取，业务处理）
        EventLoopGroup readGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(acceptGroup, readGroup);
            // 2、 选择TCP协议，NIO的实现方式
            b.channel(NioServerSocketChannel.class);
            b.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception { // channel == 一个连接 --- 连接建立
                    // 3、 职责链定义（请求收到后怎么处理）
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new LoggingHandler());
                    // TODO 3.1 增加解码X器
                    pipeline.addLast(new ProtcolHandller());
                    // TODO 3.2 打印出内容 handdler
                    pipeline.addLast(new PrintHandller()); // 添加处理器时候 指定它在哪个线程池执行
                }
            });
            // 4、 绑定端口
            System.out.println("启动成功，端口 9999");
            b.bind(9999).sync().channel().closeFuture().sync();
        } finally {
            acceptGroup.shutdownGracefully();
            readGroup.shutdownGracefully();
        }
    }
}
