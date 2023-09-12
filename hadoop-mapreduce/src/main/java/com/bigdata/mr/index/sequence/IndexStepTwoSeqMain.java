package com.bigdata.mr.index.sequence;

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
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/11
 * Author:wfm
 * Desc:
 * <p>
 * 1.输入数据
 * hello a.txt-->2
 * hello b.txt-->1
 * <p>
 * 2.输出数据
 * hello a.txt-->2 b.txt-->1
 */
public class IndexStepTwoSeqMain {
    public static void main(String[] args)throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(IndexStepTwoSeqMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(IndexStepTwoSeqMapper.class);
        job.setReducerClass(IndexStepTwoSeqReduce.class);

        // 设置输入格式
        job.setInputFormatClass(SequenceFileInputFormat.class);

        // 设置maptask端的局部聚合逻辑类
//        job.setCombinerClass(IndexStepOneReduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\index\\output4");
        if (output.exists()){
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Alearning\\data\\mr\\index\\output3"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Alearning\\data\\mr\\index\\output4"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(1);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res?0:-1);
    }


    static class IndexStepTwoSeqMapper extends Mapper<Text, Text, Text, Text> {
        Text k = new Text();
        Text v = new Text();

        // 输出<单词，文件-次数>
        @Override
        protected void map(Text key, Text value, Context context) throws IOException, InterruptedException {
            // 获取一行数据
            context.write(key, value);
        }
    }


    static class IndexStepTwoSeqReduce extends Reducer<Text, Text, Text, Text> {
        Text v = new Text();
        // 输出<单词，文件-次数拼接>
        @Override
        protected void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
            // stringbuffer是线程安全的，stringbuilder是非线程安全的，在不涉及线程安全的场景下，stringbuilder更快
            StringBuilder sb = new StringBuilder();

            for (Text value : values) {
                sb.append(value.toString()).append("\t");
            }
            v.set(sb.toString());
            context.write(key,v);
        }
    }
}
