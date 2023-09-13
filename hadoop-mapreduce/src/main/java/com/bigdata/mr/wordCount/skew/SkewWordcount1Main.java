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
import java.util.Random;

/**
 * Date:2023/9/13
 * Author:wfm
 * Desc:单词统计（解决数据倾斜问题）
 * <p>
 * 1.输入数据
 * hello hadoop
 * hello java
 * hello java
 * <p>
 * 2.输出数据
 * hello-1 2
 * hello-2 1
 * hadoop-1 1
 * java-2 1
 * java-3 1
 */
public class SkewWordcount1Main {
    public static void main(String[] args) throws Exception {
        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(SkewWordcount1Main.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(SkewWordcount1Mapper.class);
        job.setReducerClass(SkewWordcount1Reduce.class);

        // 设置maptask端的局部聚合逻辑类
        job.setCombinerClass(SkewWordcount1Reduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:/Alearning/data/mr/wc/output4");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:/Alearning/data/mr/wc/input"));
        FileOutputFormat.setOutputPath(job, new Path("C:/Alearning/data/mr/wc/output4"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(3);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }


    static class SkewWordcount1Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        Random random = new Random();
        int numReduceTasks;
        Text k = new Text();
        IntWritable v = new IntWritable(1);

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 获取reduce task个数
            numReduceTasks = context.getNumReduceTasks();
        }

        // 输出<单词-随机数,1>
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 处理一行数据
            String[] words = value.toString().split(" ");
            for (String word : words) {
                k.set(word + "-" + random.nextInt(numReduceTasks));
                context.write(k, v);
            }
        }
    }

    static class SkewWordcount1Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {
        IntWritable v = new IntWritable();

        // 输入<单词-随机数,1>,输出<单词-随机数,次数>
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Reducer<Text, IntWritable, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable value : values) {
                count += value.get();
            }
            v.set(count);
            context.write(key, v);
        }
    }
}
