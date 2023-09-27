package com.bigdata.zk.distributesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * Date:2023/9/25
 * Author:wfm
 * Desc:监控某个端口请求，有请求返回服务器的时间。
 */
public class TimeQueryService extends Thread {
    private int port;

    public TimeQueryService(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            // 启动serverSocket服务器
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("业务线程已绑定端口" + port + "准备接受客户端请求了......");
            while (true) {
                // 监听
                Socket accept = serverSocket.accept();
                // 发送数据给客户端
                OutputStream outputStream = accept.getOutputStream();
                outputStream.write(new Date().toString().getBytes()); // 发送时间给客户端
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
