package com.tony.edu.netty.jio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class TonySocketServer {
    public static void main(String[] args) throws IOException, Exception {
        // server --- jvm ===操作系统 申请端口
        ServerSocket serverSocket = new ServerSocket(9999);
        // 获取新连接
        while (true) {
            final Socket accept = serverSocket.accept();
            // accept.getOutputStream().write("推送实例");
            InputStream inputStream = accept.getInputStream();
            while (true) { // 接下来，为了完善业务，一堆的代码要去写 --0--- 拆包粘包
                byte[] request = new byte[1024];
                int read = inputStream.read(request);
                if (read == -1) {
                    break;
                }

                // 得到请求内容，解析，得到发送对象和发送内容（）
                String content = new String(request); // 可能是一次完整的数据包， 也可能是多个数据包，甚至是不完整的数据
                // 每次读取到的数据，不能够保证是一条完整的信息
                System.out.println(content);
                for (int i = 0; i < 1024 / 220; i++) {
                    byte[] message = new byte[220];
                    System.arraycopy(request,0,message,0,0); // 示意
                    // TODO 接收了一次数据， 实际会处理多次。 根据协议规定
                }
                // 不完整的数据，留着到下一次来处理

                // TODO 协议解析 -- 自定义协议【每次数据包都是固定的220字节】

                // TODO -- tomcat-- 数据处理() -- 完整的数据传递到后续的处理流程--servlet--controller--service
                // TODO 需要解决这一类问题
                if(request.length < 220) {
                    // 如果不满足一条消息，临时存储下来
                }
            }
        }
    }
}
