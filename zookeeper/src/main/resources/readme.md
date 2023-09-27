zookeeper的增删查改：com.bigdata.zk.demo.ZookeeperClientDemo

监听节点的值变化/监听节点的子节点变化（路径变化）：com.bigdata.zk.demo.ZookeeperWatchDemo

---

需求：启动多个服务器，为客户端提供时间查询。客户端随机请求某一台服务器查询时间，某台服务器挂了也不影响客户端请求需求。

代码：
- 服务器：com.bigdata.zk.distributesystem.TimeQueryServer
- 客户端：com.bigdata.zk.distributesystem.TimeQueryClient

---
需求：客户端们有顺序的执行，一个客户端处理完业务后再到另外一个客户端。

代码：com.bigdata.zk.distributeLock.DistributeLockTestClient