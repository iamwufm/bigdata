package com.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * Date:2023/8/31
 * Author:wfm
 * Desc:hdfs的API操作
 *
 * 把hadoop.tar.gz放在集群外的机器，但是跟集群能通信就可以访问集群
 * 再另外一台机器访问的话，可以用hadoop fs或hdfs dfs来访问
 *
 * 再windwon上，可以把hadoop.tar.gz的share目录下的jar包放在本地仓库
 * 当然也可以直接配置pom文从远程仓库上下载
 *
 * 此类只做一个简单操作，具体的见HdfsClientTestMian类
 *
 */
public class HdfsClientMian {

    public static void main(String[] args) throws Exception{
        /**
         * Configuration参数对象的机制：
         *      构造时，会加载jar包中的默认配置 xxx-default.xml
         *      再加载用户配置xx-site.xml，覆盖掉默认参数（可以把相应的文件放在Resources路径下）
         *      构造完成之后，还可以通过conf.set("p","v")覆盖用户配置文件中的参数值
         */
        Configuration conf = new Configuration();
        conf.set("dfs.replication","2"); // 副本数设置为2
        conf.set("dfs.blocksize","64m");// 块大小为64m


        // 1. 构造一个访问指定HDFS系统的客户端对象。以haodoop用户访问 uri填写core-site.xml配置的fs.defaultFS
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop101:8020"), conf, "hadoop");

        // 2.具体操作
        // 上传一个文件到HDFS
        fs.copyFromLocalFile(new Path("C:/Alearning/data/log/access.log.1"),new Path("/"));

        // 3.关闭资源
        fs.close();
    }
}
