package com.bigdata.zk.distributeLock;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Date:2023/9/26
 * Author:wfm
 * Desc:测试分布式锁的客户端
 * 启动多个DistributeLockTestClient，观察各个客户端的执行顺序。
 *
 * 1.启动后向zookeeper注册信息
 * 2.判断自己是否是最小节点，是就执行业务。不是监控上一个节点
 * 3.是就开始处理业务。处理完后删除节点。监控它的节点的节点就开始处理业务
 */
public class DistributeLockTestClient {
    public static void main(String[] args) throws Exception {

        DistributeLockTestClient client = new DistributeLockTestClient();

        // 1.启动后向zookeeper注册信息
        // 构建zk连接对象
        client.conncetZK();

        // 加锁：注册信息，如果是最小节点，是就执行业务。不是监控上一个节点
        client.zklock();

        // 开始处理业务
        client.doBusiness();

        // 3.是就开始处理业务。处理完后删除节点。监控它的节点的节点就开始处理业务
        // 释放锁：删除节点
        client.unZkLock();

    }

    // 构造zk连接对象
    private ZooKeeper zk;
    // 当前节点名称
    private String currentMode;
    // countDownLatch要从1减到0，主线程才开始执行。减1用：countDown()。等待计数减到0：await()
    private CountDownLatch countDownLatch;

    // 构建zk连接对象
    private void conncetZK() throws Exception {
        countDownLatch = new CountDownLatch(1);
        zk = new ZooKeeper("hadoop101:2181,hadoop102:2181,hadoop103:2181", 2000, new Watcher() {
            @Override
            public void process(WatchedEvent event) {
                // 监控节点是否被删除事件
                if (event.getState() == Event.KeeperState.SyncConnected && event.getType()== Event.EventType.NodeDeleted) {
                    try {
                        // 监控到节点有被删除，计数-1=0，执行主方法
                        // 计数 - 1
                        countDownLatch.countDown();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        });
    }

    // 加锁：加锁成功返回true，否则
    private void zklock() throws Exception {
        // 先判断注册节点的父节点是否存在，不存在则创建
        Stat stat = zk.exists("/locks", false);
        if (stat == null) {
            zk.create("/locks", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }

        // 1.创建子节点（暂时的有序列的子节点）
        currentMode = zk.create("/locks/seq-", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println("客户端向zk注册信息成功，注册的节点为：" + currentMode);

        // 2.判断节点是否是最小节点
        List<String> children = zk.getChildren("/locks", false);
        // 只有一个节点。直接获取锁
        if (children.size() == 1) {
            return;
        }

        // 对集合进行排序
        Collections.sort(children);

        System.out.println("子节点列表：" + children);

        // 获取节点名称 seq-00000000
        String thisNode = currentMode.substring("/locks/".length());
        int index = children.indexOf(thisNode); // 创建的节点在集合中排第几位
        // 如果是排在第一位。直接获取锁
        if (index == 0) {
            return;
        }

        // 需要监听它前一个节点是否被删除
        zk.getData("/locks/" + children.get(index - 1), true, null);

        // 等待计数结果为0
        countDownLatch.await();

    }

    // 业务处理
    private void doBusiness() throws Exception {
        System.out.println("准备处理业务.......");
        // 休眠10s
        Thread.sleep(10000);

        System.out.println("业务处理完成.......");
    }

    // 释放锁：删除节点
    private void unZkLock() throws Exception {
        // 参数1：节点路径 参数2：要修改的版本，-1代表任何版本
        zk.delete(currentMode, -1);

        System.out.println("节点删除成功：" + currentMode);

        // 释放资源
        zk.close();

    }
}
