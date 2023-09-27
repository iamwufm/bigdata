package com.bigdata.zk.demo;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

/**
 * Date:2023/9/25
 * Author:wfm
 * Desc:1.节点的值变化监听 2.节点的子节点变化监听（路径变化）
 * <p>
 * 使用单元测试包（junit）来操作zookeeper。点击对应test的方法，都先执行before,点击的test,after
 */
public class ZookeeperWatchDemo {
    ZooKeeper zk = null;

    @Before
    public void init() throws Exception {
        // 构建一个连接zookeeper的客户端对象
        zk = new ZooKeeper("hadoop101:2181,hadoop102:2181,hadoop103:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 节点的值变化监听
                if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.NodeDataChanged) {
                    System.out.println("节点的值");
                    System.out.println(event.getPath());// 收到事件所发生的节点路径
                    System.out.println(event.getType());// 收到事件的类型
                    System.out.println("赶紧换照片，换浴室里面的洗浴套装");// 收到事件后，我们的处理逻辑

                    try {
                        // 参数1：节点路径 参数2：是否要监听 参数3：所要获取的数据版本，null表示最新版本
                        zk.getData("/mygirls", true, null);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                } else if (event.getState() == Event.KeeperState.SyncConnected && event.getType() == Event.EventType.NodeChildrenChanged) {
                    System.out.println("子节点变化了。。。。");
                }
            }


        });
    }

    // 增加mygirls节点 create /mygirls
    // 改变/mygirls的值。在sell中执行set /mygirls "yami" 或者 在ZookeeperClientDemo类中增加修改该节点的方法
    // 在/mygirls增加子节点。在sell中执行create /mygirls/beautiful 或者 在ZookeeperClientDemo类中增加节点的方法
    @Test
    public void testGetWatch() throws Exception {
        // 参数1：节点路径 参数2：是否要监听 参数3：所要获取的数据版本，null表示最新版本
        zk.getData("/mygirls", true, null); // 监听节点的值变化事件

        // 参数1：节点路径 参数2：是否要监听
        zk.getChildren("/mygirls", true); //监听节点的子节点变化事件

        // 主线程永久休眠。启动守护线程去执行。主线程没有了，守护线程也就结束了。
        // 守护线程可参见：ThreadDemo
        Thread.sleep(Long.MAX_VALUE);
    }

}
