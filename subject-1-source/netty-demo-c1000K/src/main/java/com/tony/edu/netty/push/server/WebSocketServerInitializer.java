/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.tony.edu.netty.push.server;

import com.tony.edu.netty.push.server.handler.NewConnectHandler;
import com.tony.edu.netty.push.server.handler.WebSocketServerHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 */
public class WebSocketServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        //  职责链， 数据处理流程
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new HttpServerCodec()); // http协议编解码器 -- websocket
        pipeline.addLast(new HttpObjectAggregator(65536));
        // TODO 以下是我自己写的处理
        pipeline.addLast(new WebSocketServerHandler()); // websocket 处理器 --- 收到消息
        pipeline.addLast(new NewConnectHandler()); // 新连接建立处理器 -- 记录用户连接信息
    }
}
