package com.bigdata.mr.wordCount;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.compress.BZip2Codec;
import org.apache.hadoop.io.compress.CompressionCodec;
import org.apache.hadoop.io.compress.DefaultCodec;
import org.apache.hadoop.io.compress.SnappyCodec;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;

/**
 * Date:2023/9/5
 * Author:wfm
 * Desc:单词统计主主入口
 * 测试压缩格式
 * map端输出：snappy压缩
 * reduce端压缩：gzip或bzip
 * <p>
 * <p>
 * 以空格切分，统计单词出现次数
 * 准备：
 * 在hdfs的/input准备几份数据
 * <p>
 * 用windows安装的hadoop环境。
 * <p>
 * 文件系统用本地系统conf.set("fs.defaultFS", "file:///")
 */
public class WordcountCompressionMain {

    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        //-------------------------开启map端输出压缩-------------------------
        conf.setBoolean("mapreduce.map.output.compress", true);
        conf.setClass("mapreduce.map.output.compress.codec", SnappyCodec.class, CompressionCodec.class);

        //-------------------------开启reduce端输出压缩-------------------------
        FileOutputFormat.setCompressOutput(job, true);
        FileOutputFormat.setOutputCompressorClass(job, SnappyCodec.class);
//        FileOutputFormat.setOutputCompressorClass(job, DefaultCodec.class);
//        FileOutputFormat.setOutputCompressorClass(job, BZip2Codec.class);
// FileOutputFormat.setOutputCompressorClass(job, GzipCodec.class);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(WordcountCompressionMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        // 设置maptask端的局部聚合逻辑类
//        job.setCombinerClass(WordcountReducer.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:/Alearning/data/mr/wc/output/");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:/Alearning/data/mr/wc/input"));
        FileOutputFormat.setOutputPath(job, new Path("C:/Alearning/data/mr/wc/output"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }
}
