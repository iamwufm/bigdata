package com.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * Date:2023/8/31
 * Author:wfm
 * Desc:hdfs的API操作
 * 高可用
 */
public class HAHdfsClientMian {

    public static void main(String[] args) throws Exception {
        /**
         * Configuration参数对象的机制：
         *      构造时，会先加载jar包中的默认配置 xxx-default.xml
         *      再加载环境中的*-site.xml
         *      再加载工程中的*-site.xml（把相应的文件放在Resources路径下）
         *      构造完成之后，还可以通过conf.set("p","v")覆盖用户配置文件中的参数值
         */
        Configuration conf = new Configuration();

        // 需要把hdfs-site.xml文件放在resources，或者通过conf.set设置
        // 为了其他类避免其他类加载到hdfs-site.xml，把它命名为ha-hdfs-site.xml。再通过加载这个文件的方式
        conf.addResource("ha-hdfs-site.xml");

        // 1. 构造一个访问指定HDFS系统的客户端对象。以haodoop用户访问 uri填写core-site.xml配置的fs.defaultFS
        FileSystem fs = FileSystem.get(new URI("hdfs://mycluster"), conf, "hadoop");



        // 2.具体操作
        // 上传一个文件到HDFS
        fs.copyFromLocalFile(new Path("C:/Alearning/data/logs/accesslog/access.log"), new Path("/"));

        // 3.关闭资源
        fs.close();
    }
}
