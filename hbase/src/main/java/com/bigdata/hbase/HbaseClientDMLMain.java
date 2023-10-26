package com.bigdata.hbase;

import com.google.inject.internal.cglib.core.$LocalVariablesSorter;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnValueFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Date:2023/10/13
 * Author:wfm
 * Desc:hbase表dml操作(点击对应test的方法，都先执行before,点击的test,after)
 * 1.插入/修改数据
 * 2.查询数据-按行键查询数据
 * 3.查询数据-按行键范围查询数据
 * 4.查询数据-按列值过滤数据
 * 5.删除数据
 */
public class HbaseClientDMLMain {
    Connection conn = null;

    @Before
    public void getConn() throws Exception {
        // 构建一个连接对象
        Configuration conf = HBaseConfiguration.create();// 会自动加载hbase-site.xml
        conf.set("hbase.zookeeper.quorum", "hadoop101:2181,hadoop102:2181,hadoop103:2181");

        conn = ConnectionFactory.createConnection(conf);

    }


    // 2.插入/修改数据
    // 改:put来覆盖
    // 可在shell客户端用命令查看数据：scan 'user_info', {LIMIT => 10}
    // 或 scan 'user_info', {LIMIT => 10,FORMATTER => 'toString'}
    @Test
    public void testPut() throws Exception {
        // 1.获取一个操作指定表的table对象,进行DML操作
        Table table = conn.getTable(TableName.valueOf("user_info"));

        // 2.构造要插入的数据为一个Put类型(一个put对象只能对应一个rowkey)的对象
        Put put1 = new Put(Bytes.toBytes("001"));
        put1.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("username"), Bytes.toBytes("张三"));
        put1.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"), Bytes.toBytes(18));
        put1.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("addr"), Bytes.toBytes("北京"));


        Put put2 = new Put(Bytes.toBytes("002"));
        put2.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("username"), Bytes.toBytes("李四"));
        put2.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"), Bytes.toBytes(28));
        put2.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("addr"), Bytes.toBytes("上海"));


        ArrayList<Put> puts = new ArrayList<>();
        puts.add(put1);
        puts.add(put2);

        // 3.新增/修改数据
        table.put(puts);

        // 4.关闭资源
        table.close();
        conn.close();
    }

    // 2.查询数据-按行键查询数据
    @Test
    public void testGet() throws Exception {
        // 1.获取一个操作指定表的table对象,进行DML操作
        Table table = conn.getTable(TableName.valueOf("user_info"));

        // 2.读取行键002的数据
        Get get = new Get("002".getBytes());
        Result result = table.get(get);

        // 3. 遍历查询结果
        // 从结果中取用户指定的某个key的value
        byte[] username = result.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("username"));
        byte[] age = result.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("age"));

        String usernameStr = Bytes.toString(username);
        int age_int = Bytes.toInt(age);
        System.out.println(usernameStr);
        System.out.println(age_int);

        System.out.println("=================================");

        // 遍历整行结果中的所有kv单元格
        CellScanner cellScanner = result.cellScanner();
        while (cellScanner.advance()) {
            Cell cell = cellScanner.current();

            String row = Bytes.toString(cell.getRowArray(),cell.getRowOffset(),cell.getRowLength()); // 行键
            String family = Bytes.toString(cell.getRowArray(),cell.getFamilyOffset(),cell.getFamilyLength());
            String qualifier = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength()); //列名

            System.out.println("行键：" + row);
            System.out.println("列族名：" + family);
            System.out.println("列名：" + qualifier);
            if (qualifier.equals("age")){
                System.out.println("值：" + Bytes.toInt(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength()));
            }else {
                System.out.println("值：" + Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength()));
            }
            System.out.println("----------------------------------------");
        }

/*        Cell[] cells = result.rawCells();

        // 遍历整行结果中的所有kv单元格
        for (Cell cell : cells) {
            String row = new String(CellUtil.cloneRow(cell)); // 行键
            String family = new String(CellUtil.cloneFamily(cell)); // 列族
            String qualifier = new String(CellUtil.cloneQualifier(cell)); // 列名

            System.out.println("行键:" + row);
            System.out.println("列族:" + family);
            System.out.println("列名:" + qualifier);
            if (qualifier.equals("age")) {
                int value = Bytes.toInt(CellUtil.cloneValue(cell));
                System.out.println("值：" + value);

            } else {
                String value = new String(CellUtil.cloneValue(cell));
                System.out.println("值：" + value);
            }
            System.out.println("-------------------------");
        }*/

        // 4.关闭资源
        table.close();
        conn.close();
    }

    /**
     * 3.查询数据-按行键范围查询数据
     * 先执行testManyPuts方法，批量插入数据
     *
     * 行键按照字典顺序排序（字符串），可以查看ASCII字符代码表
     * 10       [0001 0011][0000 0011]
     * 100      [0001 0011][0000 0011][0000 0011]
     * 1000     [0001 0011][0000 0011][0000 0011][0000 0011]
     * 10000    [0001 0011][0000 0011][0000 0011][0000 0011][0000 0011]
     * 11       [0001 0011][0001 0011]
     * 110      [0001 0011][0001 0011][0000 0011]
     * 1100     [0001 0011][0001 0011][0000 0011][0000 0011]
     * 11000    [0001 0011][0001 0011][0000 0011][0000 0011][0000 0011]
     * 12       [0001 0011][0010 0011]
     */
    @Test
    public void testScan() throws Exception {
        // 1.获取一个操作指定表的table对象,进行DML操作
        Table table = conn.getTable(TableName.valueOf("user_info"));

        // 包含起始行键，不包含结束行键,但是如果真的想查询出末尾的那个行键，那么，可以在末尾行键上拼接一个不可见的字节（\000）
        Scan scan = new Scan();
        scan.withStartRow(Bytes.toBytes("10"));
        scan.withStopRow(Bytes.toBytes("10000\001"));

        // 读取多行数据 获得 scanner
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner) {
            // 遍历整行结果中的所有kv单元格
            CellScanner cellScanner = result.cellScanner();
            while (cellScanner.advance()) {
                Cell cell = cellScanner.current();

                String row = Bytes.toString(cell.getRowArray(),cell.getRowOffset(),cell.getRowLength()); // 行键
                String family = Bytes.toString(cell.getRowArray(),cell.getFamilyOffset(),cell.getFamilyLength());
                String qualifier = Bytes.toString(cell.getQualifierArray(),cell.getQualifierOffset(),cell.getQualifierLength()); //列名

                System.out.println("行键：" + row);
                System.out.println("列族名：" + family);
                System.out.println("列名：" + qualifier);
                if (qualifier.equals("age")){
                    System.out.println("值：" + Bytes.toInt(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength()));
                }else {
                    System.out.println("值：" + Bytes.toString(cell.getValueArray(),cell.getValueOffset(),cell.getValueLength()));
                }
            }

            System.out.println("----------------------------------------");
        }
        // 4.关闭资源
        table.close();
        conn.close();
    }

    // 4.查询数据-按列值过滤数据
    @Test
    public void TestScanWithFilter() throws Exception {
        // 1.获取一个操作指定表的table对象,进行DML操作
        Table table = conn.getTable(TableName.valueOf("user_info"));
        // 2.扫描数据
        Scan scan = new Scan();
        //增加过滤器，ColumnValueFilter 只能显示出一列，SingleColumnValueFilter 能够显示出来所有的列
        // 获取列族为base_info,age=30的数据
//        ColumnValueFilter filter = new ColumnValueFilter(Bytes.toBytes("base_info"), Bytes.toBytes("age"), CompareOperator.EQUAL, Bytes.toBytes(30));
        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("base_info"), Bytes.toBytes("age"),CompareOperator.EQUAL, Bytes.toBytes(30));

        // 添加过滤器
        scan.setFilter(filter);
        ResultScanner res = table.getScanner(scan);
        for(Result r:res){
            byte[] name = r.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("username"));
            byte[] age = r.getValue(Bytes.toBytes("base_info"), Bytes.toBytes("age"));
            String name_str = Bytes.toString(name);
            int age_int = Bytes.toInt(age);
            System.out.println(name_str+","+age_int);
        }

        // 关闭资源
        table.close();
        conn.close();
    }


    /**
     * 循环插入大量数据
     * 查看有多少行数据：count 'user_info'
     *
     * @throws Exception
     */
    @Test
    public void testManyPuts() throws Exception {

        Table table = conn.getTable(TableName.valueOf("user_info"));
        ArrayList<Put> puts = new ArrayList<>();

        for (int i = 0; i < 100000; i++) {
            Put put = new Put(Bytes.toBytes("" + i));
            put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("username"), Bytes.toBytes("张三" + i));
            put.addColumn(Bytes.toBytes("base_info"), Bytes.toBytes("age"), Bytes.toBytes((18 + i)));
            put.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("addr"), Bytes.toBytes("北京"));

            puts.add(put);
        }

        table.put(puts);

        // 关闭资源
        table.close();
        conn.close();

    }

    // 5.删除表
    @Test
    public void testDelete() throws Exception {
        Table table = conn.getTable(TableName.valueOf("user_info"));

        // 构造一个对象封装要删除的数据信息

        // 删除行键001的数据
        Delete delete1 = new Delete(Bytes.toBytes("001"));

        // 删除行键002的extra_info列族中的addr数据
        Delete delete2 = new Delete(Bytes.toBytes("002"));
        delete2.addColumn(Bytes.toBytes("extra_info"), Bytes.toBytes("addr"));

        ArrayList<Delete> dels = new ArrayList<>();
        dels.add(delete1);
        dels.add(delete2);

        table.delete(dels);

        // 关闭资源
        table.close();
        conn.close();
    }



}
