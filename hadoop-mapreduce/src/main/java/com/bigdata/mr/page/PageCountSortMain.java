package com.bigdata.mr.page;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:对网址的访问次数进行降序排序（输入数据已经统计好每个网页的访问次数）
 */
public class PageCountSortMain {
    public static void main(String[] args) throws Exception {
        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(PageCountSortMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(PageCountSortMapper.class);
        job.setReducerClass(PageCountSortReduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(PageCount.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(PageCount.class);
        job.setOutputValueClass(NullWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\page\\output4");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\page\\output3"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\page\\output4"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(1);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);
    }

    // map阶段，输出 pageCount对象，null
    public static class PageCountSortMapper extends Mapper<LongWritable, Text, PageCount, NullWritable> {
        PageCount pageCount = new PageCount();
        NullWritable v = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

            // 处理一行数据
            String[] words = value.toString().split("\t");

            pageCount.set(words[0], Integer.parseInt(words[1]));

            context.write(pageCount, v);

        }
    }

    // reduce阶段，输出 pageCount对象，null
    public static class PageCountSortReduce extends Reducer<PageCount, NullWritable, PageCount, NullWritable> {
        NullWritable v = NullWritable.get();

        @Override
        protected void reduce(PageCount key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
            // 处理一组数据
            context.write(key, v);
        }

    }
}
