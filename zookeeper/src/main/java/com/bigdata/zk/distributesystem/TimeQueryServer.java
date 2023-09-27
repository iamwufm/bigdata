package com.bigdata.zk.distributesystem;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * Date:2023/9/25
 * Author:wfm
 * Desc:事件查询服务器
 * 输入两个参数，参数1:ip地址(启动服务器的ip地址)，参数2:端口号（避免端口号冲突）
 * 1.服务器启动去zookeeper注册信息 hostname:port
 * 2.启动业务线程开始处理业务（有请求返回服务器的时间）
 * <p>
 * idea启动方式：
 * 1.查询本机的ip。cmd 输入ipconfig。如192.168.10.6
 * 2.在TimeQueryServer的 edit configurations 添加两个参数（空格隔开 ip 端口号） 192.168.10.6 7777
 * 多启动几个（新版idea怎么同时打开多个窗口可在百度搜《idea如何开启多个客户端》）
 * 3.启动com.bigdata.zk.distributesystem.TimeQueryClient
 *
 * linux启动方式：
 * 1.package打包程序。jar包在target下，把包打包上传到linux
 *
 * 2.在hadoop101、hadoop102和hadoop103启动服务器。java -cp 包名.jar 类名 参数1 参数2
 * java -cp zookeeper-jar-with-dependencies.jar com.bigdata.zk.distributesystem.TimeQueryServer hadoop101 7777
 * java -jar zookeeper-jar-with-dependencies.jar hadoop102 7777 (在pom文件指定了类名。可以采用这个方式)
 * java -jar zookeeper-jar-with-dependencies.jar hadoop103 7777
 *
 * 3.在其中一台服务器启动客户端。java -cp 包名.jar 类名
 * java -cp zookeeper-jar-with-dependencies.jar com.bigdata.zk.distributesystem.TimeQueryClient
 *
 */
public class TimeQueryServer {
    public static void main(String[] args) throws Exception {
        // 1.服务器启动去zookeeper注册信息 host:port
        TimeQueryServer timeQueryServer = new TimeQueryServer();

        // 构建zk客户端连接
        timeQueryServer.connnectZk();

        // 注册服务器信息。参数1:ip地址(启动服务器的ip地址)，参数2:端口号（避免端口号冲突）
        timeQueryServer.registerServerInfo(args[0], args[1]);

        // 2.启动业务线程开始处理业务（有请求返回返回服务器的事件）
        new TimeQueryService(Integer.parseInt(args[1])).start();
    }

    private ZooKeeper zk;

    // 构建zk客户端
    private void connnectZk() throws Exception {
        zk = new ZooKeeper("hadoop101:2181,hadoop102:2181,hadoop103:2181", 2000, null);

    }

    // 注册服务器信息
    private void registerServerInfo(String hostname, String port) throws Exception {
        // 先判断注册节点的父节点是否存在，不存在则创建
        Stat stat = zk.exists("/servers", false);
        if (stat == null) {
            zk.create("/servers", null, Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 注册服务器数据到zk的约定注册节点下
        String create = zk.create("/servers/server", (hostname + ":" + port).getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(hostname + "服务器向zk注册信息成功，注册的节点为：" + create);
    }


}
