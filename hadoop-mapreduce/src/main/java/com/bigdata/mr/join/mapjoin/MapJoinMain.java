package com.bigdata.mr.join.mapjoin;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:订单数据关联用户数据。类似的sql：`select a.*,b.* from a join b on a.uid=b.uid;`
 * 1.输入数据
 * # 订单数据
 * order001,u001
 * order002,u001
 * order003,u005
 * <p>
 * #用户数据
 * u001,senge,18,angelababy
 * u002,laozhao,48,ruhua
 * u003,xiaoxu,16,chunge
 * <p>
 * 2.输出数据
 * order001,u001,senge,18,angelababy
 * order002,u001,laozhao,48,ruhua
 * order003,u003,xiaoxu,16,chunge
 * <p>
 * <p>
 * 在map阶段处理完数据，无reduce task
 */
public class MapJoinMain {
    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(MapJoinMain.class);

        // 缓存普通文件到 Task 运行节点。
        job.addCacheFile(new URI("file:///C:/Alearning/data/mr/join/mapjoin/user/user.txt"));

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(MapJoinMapper.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(NullWritable.class);


        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\join\\mapjoin\\output");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\join\\mapjoin\\order"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\join\\mapjoin\\output"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(0);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }

    static class MapJoinMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
        private Map<String, String> userMap = new HashMap<>();
        private Text k = new Text();
        private NullWritable v = NullWritable.get();

        // 在任务开始前将user数据缓存进userMap
        @Override
        protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            // 通过缓存文件得到小表数据
            URI[] cacheFiles = context.getCacheFiles();
            Path path = new Path(cacheFiles[0]);

            // 获取文件系统对象，并开流
            FileSystem fs = FileSystem.get(context.getConfiguration());
            FSDataInputStream in = fs.open(path);

            // 通过包装流转换为 reader,方便按行读取
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "utf-8"));

            String line = null;

            // 逐行读取，按行处理
            while ((line = br.readLine()) != null) {

                int index = line.indexOf(",");
                // 添加到userMap
                // line值为u001,senge,18,angelababy <u001,"senge,18,angelababy">
                userMap.put(line.substring(0, index), line.substring(index + 1));
            }

            br.close();
            in.close();
            fs.close();
        }

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, NullWritable>.Context context) throws IOException, InterruptedException {
            // 处理一行数据
            String line = value.toString();
            String[] words = line.split(",");
            // 获取map值
            String user = userMap.get(words[1]);

            // 拼接信息
            k.set(line + "," + user);
            context.write(k, v);
        }
    }
}
