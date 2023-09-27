package com.bigdata.zk.demo;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

/**
 * Date:2023/9/25
 * Author:wfm
 * Desc:zookeeper的增删查改操作
 * 使用单元测试包（junit）来操作zookeeper。点击对应test的方法，都先执行before,点击的test,after
 */
public class ZookeeperClientDemo {
    ZooKeeper zk = null;

    @Before
    public void init() throws Exception {
        // 构建一个连接zookeeper的客户端对象
        zk = new ZooKeeper("hadoop101:2181,hadoop102:2181,hadoop103:2181", 2000, null);
    }


    // 1.创建节点
    @Test
    public void testCreate() throws Exception {
        // 参数1：要创建节点的路径 参数2：节点内容 参数3：访问权限 参数四：节点类型
        String create = zk.create("/idea", "hello idea".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        System.out.println(create);

        // 创建子节点
        zk.create("/idea/test2", "hello test".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);

        // 关闭资源
        zk.close();
    }

    // 2.修改节点的值
    @Test
    public void testUpdate()throws Exception{
        // 参数1：节点路径 参数2：节点路径的内容 参数3：要修改的版本，-1代表任何版本
        zk.setData("/idea","我爱你".getBytes("UTF-8"),-1);

        // 关闭资源
        zk.close();
    }

    // 3.获取节点的信息
    @Test
    public void testGet()throws Exception{
        // 参数1：节点路径 参数2：是否要监听 参数3：所要获取的数据版本，null表示最新版本
        byte[] data = zk.getData("/idea", false, null);
        System.out.println(new String(data,"UTF-8"));

        // 关闭资源
        zk.close();
    }

    // 4.获取子节点的名称
    @Test
    public void testListChildren() throws Exception{
        // 参数1：节点路径 参数2：是否监听
        List<String> children = zk.getChildren("/idea", false);

        for (String child : children) {
            System.out.println(child);
        }

        // 关闭资源
        zk.close();
    }

    // 5.删除节点
    @Test
    public void testRm() throws Exception{
        // 参数1：节点路径 参数2：要修改的版本，-1代表任何版本
        zk.delete("/idea/test2",-1);
        // 关闭资源
        zk.close();
    }


}
