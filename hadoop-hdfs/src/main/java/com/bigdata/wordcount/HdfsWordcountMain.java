package com.bigdata.wordcount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Date:2023/9/4
 * Author:wfm
 * Desc: 统计某目录下所有文件的单词次数，文件内容以空格切开
 * 1.连接hdfs，获取某目录下的所有文件
 * 2.遍历所有的文件
 * 3.对文件的每一行进行数据处理
 * 4.把结果写入hdfs
 */
public class HdfsWordcountMain {

    public static void main(String[] args) throws Exception {

        Properties pros = new Properties();
        pros.load(HdfsWordcountMain.class.getClassLoader().getResourceAsStream("job.properties"));
        Path input = new Path(pros.getProperty("INPUT_PATH"));
        Path output = new Path(pros.getProperty("OUTPUT_PATH"));

        Class<?> mapper_class = Class.forName(pros.getProperty("MAPPER_CLASS"));
        Mapper mapper = (Mapper) mapper_class.newInstance();

        Context context = new Context();

        // 1.连接hdfs，获取某目录下的所有文件
        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop101:8020"), new Configuration(), "hadoop");
        RemoteIterator<LocatedFileStatus> iter = fs.listFiles(input, false);

        // 2.遍历所有的文件
        while (iter.hasNext()) {
            // 获取文件
            LocatedFileStatus file = iter.next();
            FSDataInputStream in = fs.open(file.getPath());
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String line = null;
            // 逐行读取，并处理
            while ((line = br.readLine()) != null) {
                // 3.对文件的每一行进行数据处理
                // 调用一个方法对每一行进行业务处理
                mapper.map(line, context);
            }

            // 关闭资源
            br.close();
            in.close();
        }

        // 4.把结果写入hdfs
        // 获取结果
        HashMap<Object, Object> contexMap = context.getContexMap();

        if (fs.exists(output)) {
            throw new RuntimeException("指定的输出目录已存在，请更换...!");
        }

        FSDataOutputStream out = fs.create(new Path(output, new Path("res.dat")));

        Set<Map.Entry<Object, Object>> entries = contexMap.entrySet();

        for (Map.Entry<Object, Object> entry : entries) {
            out.write((entry.getKey().toString() + "\t" + entry.getValue() + "\n").getBytes());
        }

        // 关闭资源
        out.close();
        fs.close();

        System.out.println("恭喜！数据统计完成.....");


    }
}
