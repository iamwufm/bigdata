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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/11
 * Author:wfm
 * Desc: 统计出每个单词在每个文件中的总次数
 * <p>
 * 1.输入数据
 * # a.txt
 * hello tom
 * hello java
 * <p>
 * # b.txt
 * hello jack
 * <p>
 * 2.输出数据
 * hello a.txt-->2
 * hello b.txt-->1
 */
public class IndexStepOneSeqMain {

    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(IndexStepOneSeqMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(IndexStepOneSeqMapper.class);
        job.setReducerClass(IndexStepOneSeqReduce.class);

        // 设置输出格式
        job.setOutputFormatClass(SequenceFileOutputFormat.class);

        // 设置maptask端的局部聚合逻辑类
//        job.setCombinerClass(IndexStepOneReduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\index\\output3");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\index\\input"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\index\\output3"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(1);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }

    static class IndexStepOneSeqMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        String fileName;
        Text k = new Text();
        IntWritable v = new IntWritable(1);

        // 在执行map方法前先执行一次。可以做一些初始化配置
        @Override
        protected void setup(Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {
            // 从输入切片信息中获取当前正在处理的一行数据所属的文件
            FileSplit inputSplit = (FileSplit) context.getInputSplit();
            fileName = inputSplit.getPath().getName();
        }

        // 输出<单词-文件,1>
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, IntWritable>.Context context) throws IOException, InterruptedException {

            // 切割数据
            String[] words = value.toString().split(" ");

            for (String word : words) {
                k.set(word + "-" + fileName);
                context.write(k, v);
            }

        }
    }


    static class IndexStepOneSeqReduce extends Reducer<Text, IntWritable, Text, Text> {
        Text k = new Text();
        Text v = new Text();

        // 输出<单词,文件-->次数>
        // 输入<单词-文件,1>
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable value : values) {
                count += value.get();
            }
            String[] words = key.toString().split("-");
            k.set(words[0]);
            v.set(words[1] + "-->" + count);
            context.write(k, v);
        }
    }
}
