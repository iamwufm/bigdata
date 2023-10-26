package com.bigdata.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.regionserver.BloomType;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

/**
 * Date:2023/10/13
 * Author:wfm
 * Desc:hbase表ddl操作(点击对应test的方法，都先执行before,点击的test,after)
 * 1.建表
 * 2.修改表定义--修改一个列族信息
 * 3.删除表
 */
public class HbaseClientDDLMain {

    Connection conn = null;

    @Before
    public void getConn() throws Exception {
        // 构建一个连接对象
        Configuration conf = HBaseConfiguration.create();// 会自动加载hbase-site.xml
        conf.set("hbase.zookeeper.quorum", "hadoop101:2181,hadoop102:2181,hadoop103:2181");

        conn = ConnectionFactory.createConnection(conf);

    }

    // 建表
    // 相当于：create 'user_info', {NAME => 'base_info'},{NAME => 'extra_info', VERSIONS => 3}
    @Test
    public void testCreateTable() throws Exception {
        // 1.从连接中构造一个DDL操作器
        Admin admin = conn.getAdmin();
        // 2.创建一个表定义描述对象
        TableDescriptorBuilder user_info = TableDescriptorBuilder.newBuilder(TableName.valueOf("user_info"));

        // 3.创建列族描述的建造者
        ColumnFamilyDescriptorBuilder columnFamily1 = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("base_info"));
        ColumnFamilyDescriptorBuilder columnFamily2 = ColumnFamilyDescriptorBuilder.newBuilder(Bytes.toBytes("extra_info"));
        columnFamily2.setMaxVersions(3);// 设置该列族中存储数据的最大版本数,默认是1

        // 创建添加完参数的列族描述
        user_info.setColumnFamily(columnFamily1.build());
        user_info.setColumnFamily(columnFamily2.build());

        // 4. 用ddl操作器对象：admin 来建表
        admin.createTable(user_info.build());

        // 5.关闭资源
        admin.close();
        conn.close();
    }

    // 修改表定义--修改一个列族信息
    // 通过 desc 'user_info'查看
    // 前：{NAME => 'extra_info', INDEX_BLOCK_ENCODING => 'NONE', VERSIONS => '3',  。。。, BLOOMFILTER => 'ROW', 。。。}
    // 后：{NAME => 'other_info', INDEX_BLOCK_ENCODING => 'NONE', VERSIONS => '3', 。。。, BLOOMFILTER => 'ROWCOL', 。。。}
    @Test
    public void testAlterTable() throws Exception {
        // 1.从连接中构造一个DDL操作器
        Admin admin = conn.getAdmin();

        // 2.创建一个表格描述建造者，填写取出旧的表定义信息
        TableDescriptor tableDescriptor = admin.getDescriptor(TableName.valueOf("user_info"));
        TableDescriptorBuilder tableDescriptorBuilder = TableDescriptorBuilder.newBuilder(tableDescriptor);

        // 获取存在的列族
        ColumnFamilyDescriptor other_info = tableDescriptor.getColumnFamily(Bytes.toBytes("extra_info"));

        // 3.创建列族描述建造者，填写旧的列族描述
        ColumnFamilyDescriptorBuilder columnFamily = ColumnFamilyDescriptorBuilder.newBuilder(other_info);
        columnFamily.setBloomFilterType(BloomType.ROWCOL); // 设置该列族的布隆过滤器类型

        // 创建添加完参数的列族描述
        tableDescriptorBuilder.modifyColumnFamily(columnFamily.build());

        // 4.将修改过的表定义交给admin去提交
        admin.modifyTable(tableDescriptorBuilder.build());

        // 5.关闭资源
        admin.close();
        conn.close();
    }

    // 删除表
    @Test
    public void testDropTable() throws Exception {
        // 1.从连接中构造一个DDL操作器
        Admin admin = conn.getAdmin();

        // 2.停用表
        admin.disableTable(TableName.valueOf("user_info"));
        // 3.删除表
        admin.deleteTable(TableName.valueOf("user_info"));

        // 4.关闭资源
        admin.close();
        conn.close();
    }
}
