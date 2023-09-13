package com.bigdata.mr.wordCount.skew;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/13
 * Author:wfm
 * Desc:单词统计（解决数据倾斜问题）
 * <p>
 * 1.输入数据
 * hello-1 2
 * hello-2 1
 * hadoop-1 1
 * java-2 1
 * java-3 1
 * <p>
 * 2.输出数据
 * hello 3
 * hadoop 1
 * java 2
 */
public class SkewWordcount2Main {
    public static void main(String[] args) throws Exception {
        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(SkewWordcount2Main.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(SkewWordcount2Mapper.class);
        job.setReducerClass(SkewWordcount2Reduce.class);

        // 设置maptask端的局部聚合逻辑类
        job.setCombinerClass(SkewWordcount2Reduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:/Alearning/data/mr/wc/output5");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:/Alearning/data/mr/wc/output4"));
        FileOutputFormat.setOutputPath(job, new Path("C:/Alearning/data/mr/wc/output5"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(1);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }

    static class SkewWordcount2Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        Text k = new Text();
        IntWritable v = new IntWritable();

        // 输出 <单词,次数>
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 处理一行数据
            String[] words = value.toString().split("\t");
            k.set(words[0].split("-")[0]);
            v.set(Integer.parseInt(words[1]));
            context.write(k, v);
        }
    }

    static class SkewWordcount2Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        IntWritable v = new IntWritable();

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 处理一组数据
            int count = 0;

            for (IntWritable value : values) {
                count += value.get();
            }
            v.set(count);
            context.write(key, v);
        }
    }
}
