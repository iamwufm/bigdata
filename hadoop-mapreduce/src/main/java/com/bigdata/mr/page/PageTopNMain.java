package com.bigdata.mr.page;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:
 */
public class PageTopNMain {

    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        conf.addResource("test.xml");

        Job job = Job.getInstance(conf);


        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(PageTopNMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(PageCountMapper.class);
        job.setReducerClass(PageTopNReduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\page\\output2");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\page\\input"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\page\\output2"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(1);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

    }
}
