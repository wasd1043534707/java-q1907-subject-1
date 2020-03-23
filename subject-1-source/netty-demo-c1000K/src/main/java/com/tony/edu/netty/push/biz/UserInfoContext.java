package com.tony.edu.netty.push.biz;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.concurrent.ConcurrentHashMap;

public class UserInfoContext {
    static ConcurrentHashMap<String, Channel> userInfos = new ConcurrentHashMap<String, Channel>();

    // 保存信息
    public static void saveConnection(String userId, Channel channel) {
        userInfos.put(userId, channel);
    }

    // 退出的时候移除掉
    public static void removeConnection(Object userId) {
        if (userId != null) {
            userInfos.remove(userId.toString());
        }
    }

    public static void send(String userId, String content) {
        Channel channel = userInfos.get(userId);
        if (channel == null) {
            return;
        }
        if (!channel.isActive()) {
            userInfos.remove(userId);
            return;
        }
        channel.eventLoop().execute(() -> {
            channel.writeAndFlush(new TextWebSocketFrame(content)); // 推送1024字节
        });
    }
}
