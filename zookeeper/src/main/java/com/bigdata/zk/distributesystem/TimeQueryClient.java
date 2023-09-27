package com.bigdata.zk.distributesystem;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Date:2023/9/25
 * Author:wfm
 * Desc:客户端请求时间查询
 * 1.查询在线服务器列表
 * 2.处理业务（向其中一台服务发送时间查询请求）
 */
public class TimeQueryClient {
    public static void main(String[] args) throws Exception {
        // 1.查询在线服务器列表
        TimeQueryClient client = new TimeQueryClient();
        // 构建zk连接对象
        client.conncetZK();
        // 查询在线服务器列表
        client.getOneLineServers();

        // 2.处理业务（向其中一台服务发送时间查询请求）
        client.sendRequest();

    }

    // 定义一个list用于存放最新的在线服务器列表
    private volatile ArrayList<String> oneLineServers = new ArrayList<>();

    // 构造zk连接对象
    private ZooKeeper zk;

    // 构建zk连接对象
    private void conncetZK() throws Exception {
        zk = new ZooKeeper("hadoop101:2181,hadoop102:2181,hadoop103:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 子节点有变化
                if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.NodeChildrenChanged) {
                    try {
                        // 事件回调逻辑，再次查询zk上的在线服务器节点即可，查询逻辑中又再次注册了子节点变化事件监听
                        getOneLineServers();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }

    // 查询在线服务器列表
    private void getOneLineServers() throws Exception {
        // 注册监听
        List<String> children = zk.getChildren("/servers", true);

        ArrayList<String> servers = new ArrayList<>();
        // 遍历现在子节点，获取子节点的数据信息，存入在线服务器列表
        for (String child : children) {
            byte[] data = zk.getData("/servers/" + child, false, null);
            String serverInfo = new String(data);
            servers.add(serverInfo);
        }

        oneLineServers = servers;
        System.out.println("查询了一次zk，当前在线的服务器有：" + servers);

    }

    // 2.处理业务（向其中一台服务发送时间查询请求）
    private void sendRequest() throws Exception{
        Random random = new Random();

        while (true) {
            try {
                // 随机挑选一台在线服务器
                int randNum = random.nextInt(oneLineServers.size());
                String server = oneLineServers.get(randNum);

                System.out.println("本次请求挑选的服务器为:" + server);

                // 获取hostname和port信息
                String hostname = server.split(":")[0];
                int port = Integer.parseInt(server.split(":")[1]);

                // 创建Socket对象（根据ip地址和端口号，接收服务端发送的数据）
                Socket socket = new Socket(hostname, port);

                // 接收服务端发送的数据
                InputStream inputStream = socket.getInputStream();
                byte[] buf = new byte[256];
                int read = inputStream.read(buf);
                System.out.println("服务器响应的时间为：" + new String(buf, 0, read));

                inputStream.close();
                socket.close();

                // 休眠2s
                Thread.sleep(2000);

            } catch (Exception e) {
                e.printStackTrace();
                // 休眠20s
                Thread.sleep(20000);
            }
        }
    }
}
