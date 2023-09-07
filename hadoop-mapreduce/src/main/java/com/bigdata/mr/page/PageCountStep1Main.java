package com.bigdata.mr.page;

import com.bigdata.mr.flow.FlowBean;
import com.bigdata.mr.flow.FlowCountMapper;
import com.bigdata.mr.flow.FlowCountReduce;
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
import java.util.Iterator;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:统计网址的访问次数
 */
public class PageCountStep1Main {

    public static void main(String[] args) throws Exception {
        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(PageCountStep1Main.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(PageCountStep1Mapper.class);
        job.setReducerClass(PageCountStep1Reduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\page\\output3");
        if (output.exists()){
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Alearning\\data\\mr\\page\\input"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Alearning\\data\\mr\\page\\output3"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);
    }

    // mapper阶段 输出 网址,1
    public static class PageCountStep1Mapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            // 处理一行数据
            String[] words = value.toString().split(" ");

            context.write(new Text(words[1]), new IntWritable(1));
        }
    }

    // reduce阶段 输出 网址,访问次数
    public static class PageCountStep1Reduce extends Reducer<Text, IntWritable, Text, IntWritable> {

        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            // 处理一组数据
            Iterator<IntWritable> iterator = values.iterator();
            while (iterator.hasNext()){
                count += iterator.next().get();
            }

            // 写
            context.write(key,new IntWritable(count));
        }
    }
}
