package com.bigdata.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Arrays;

/**
 * Date:2023/8/31
 * Author:wfm
 * Desc: 使用单元测试包（junit）来操作hdfs。可以直接在本类中run。
 * 点击对应test的方法，都先执行before,点击的test,after
 * 1.创建文件夹
 * 2.文件上传
 * 3.文件下载
 * 4.文件的更名或移动
 * 5.查看文件
 * 6.删除文件夹
 * <p>
 * 准备工作
 * 1./test不存在
 * 2.准备三份数据：C:/Alearning/data/logs/accesslog/access.log.1
 * C:/Alearning/data/logs/accesslog/access.log.3
 * C:/Alearning/software/jdk/jdk-8u144-linux-x64.tar.gz
 */
public class HdfsClientTestMian {

    FileSystem fs = null;


    // 第一执行的
    @Before
    public void init() throws Exception {

        Configuration conf = new Configuration();
        conf.set("dfs.replication", "2"); // 副本数设置为2
        conf.set("dfs.blocksize", "64m");// 块大小为64m

        // 1. 构造一个访问指定HDFS系统的客户端对象。以haodoop用户访问 uri填写core-site.xml配置的fs.defaultFS
        fs = FileSystem.get(new URI("hdfs://hadoop101:8020"), conf, "hadoop");

        System.out.println("客户端对象创建成功" + System.currentTimeMillis());

    }

    // 1.创建文件夹
    @Test
    public void testMkdirs() throws Exception {

        // 创建文件夹/test
        fs.mkdirs(new Path("/test"));

        System.out.println("/test文件夹创建成功" + System.currentTimeMillis());

    }

    // 2.文件上传
    @Test
    public void testCopyFromLocalFile() throws Exception {

        // 上传文件/test
        fs.copyFromLocalFile(new Path("C:/Alearning/data/logs/accesslog/access.log.1"), new Path("/test"));
        fs.copyFromLocalFile(new Path("C:/Alearning/data/logs/accesslog/access.log.3"), new Path("/test"));
        fs.copyFromLocalFile(new Path("C:/Alearning/software/jdk/jdk-8u144-linux-x64.tar.gz"), new Path("/test"));

        System.out.println("文件上传成功" + System.currentTimeMillis());

    }

    // 3.文件下载
    @Test
    public void testCopyToLocalFile() throws Exception {

        // 从/test下载access.log.3文件
        fs.copyToLocalFile(false, new Path("/test/access.log.3"), new Path("C:/Alearning/data"), false);

        System.out.println("文件下载成功" + System.currentTimeMillis());
    }


    // 4.文件的更名或移动
    @Test
    public void testRename() throws Exception {

        // 把/test/access.log.3 中文件改名为test.txt
        fs.rename(new Path("/test/access.log.3"), new Path("/test/test.txt"));
        fs.rename(new Path("/test/access.log.1"), new Path("/"));
        System.out.println("文件的更名或移动" + System.currentTimeMillis());

    }

    // 5.查看文件
    @Test
    public void testLF() throws Exception {
        // 只查询文件的信息,不返回文件夹的信息
        RemoteIterator<LocatedFileStatus> iter = fs.listFiles(new Path("/"), true);

        while (iter.hasNext()) {
            LocatedFileStatus status = iter.next();
            System.out.println("文件全路径：" + status.getPath());
            System.out.println("块大小：" + status.getBlockSize());
            System.out.println("文件长度：" + status.getLen());
            System.out.println("副本数量：" + status.getReplication());
            System.out.println("块信息：" + Arrays.toString(status.getBlockLocations()));

            System.out.println("--------------------------------");
        }
    }

    // 5.查看文件
    @Test
    public void testLs2() throws Exception {
        FileStatus[] listStatus = fs.listStatus(new Path("/"));

        for (FileStatus status : listStatus) {
            System.out.println("文件全路径：" + status.getPath());

            System.out.println(status.isDirectory() ? "这是文件夹" : "这是文件");
            if (!status.isDirectory()) {
                System.out.println("块大小：" + status.getBlockSize());
                System.out.println("文件长度：" + status.getLen());
                System.out.println("副本数量：" + status.getReplication());

                System.out.println("--------------------------------");
            }
        }
    }

    // 6.删除文件夹
    @Test
    public void testDelete() throws Exception {

        // 删除文件夹
        fs.delete(new Path("/test"), true);

        System.out.println("删除文件夹成功" + System.currentTimeMillis());
    }

    // 7.读取hdfs中文件的内容
    @Test
    public void testReadData() throws IOException {
        FSDataInputStream in = fs.open(new Path("/input/test1.txt"));

        BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));

        String line = null;

        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        br.close();
        in.close();

    }

    // 读取hdfs中文件的指定偏移量范围的内容
    @Test
    public void testRandomReadData() throws IOException {

        FSDataInputStream in = fs.open(new Path("/input/test1.txt"));

        // 将读取的起始位置进行指定(0开始)
        in.seek(12);
        // 读16个字节
        byte[] buf = new byte[11];
        in.read(buf);

        System.out.println(new String(buf));

        in.close();

    }

    // 往hdfs中的文件写内容
    @Test
    public void testWriteData() throws IOException {
        // hdfs输出流
        FSDataOutputStream out = fs.create(new Path("/aa.txt"), false);

        // 本地文件输入流
        FileInputStream in = new FileInputStream("C:/Alearning/data/logs/accesslog/access.log");

        byte[] buf = new byte[1024];
        int read = 0;
        while ((read = in.read(buf)) != -1) {
            out.write(buf, 0, read);
        }

        in.close();
        out.close();
    }

    // 最后一步关闭资源
    @After
    public void lastStep() throws Exception {
        fs.close();

        System.out.println("资源已关闭" + System.currentTimeMillis());
    }
}
