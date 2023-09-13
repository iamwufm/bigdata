package com.bigdata.mr.wordCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/**
 * Date:2023/9/5
 * Author:wfm
 * Desc: 单词统计主主入口
 * 以空格切分，统计单词出现次数
 * <p>
 * 程序写完记得打包 package，后右键点击run
 * 准备：
 * 在hdfs的/input准备几份数据
 * <p>
 * 用于提交mapreduce job的客户端程序
 * 功能：
 * 1.封装本次job运行时所需要的必要参数
 * 2.跟yarn进行交互，将mapreduce程序成功的启动运行
 */
public class JobSubmitterWindowsToYarn {

    public static void main(String[] args) throws Exception {
        // 在代码中设置JVM系统参数，用于给job对象来获取访问HDFS的用户身份
        System.setProperty("HADOOP_USER_NAME", "hadoop");

        Configuration conf = new Configuration();
        // 设置job运行时要访问的默认文件系统
        conf.set("fs.defaultFS", "hdfs://hadoop101:8020");
        // 设置job提交到哪里运行,默认时local
        conf.set("mapreduce.framework.name", "yarn");
        // resoucemanager的机器
        conf.set("yarn.resourcemanager.hostname", "hadoop102");
        // 如果要从windows系统上运行这个job提交客户端程序，则需要夹这个跨平台提交参数
        conf.set("mapreduce.app-submission.cross-platform", "true");

        // 1.创建job对象
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJar("C:/code/bigdata/hadoop-mapreduce/target/wc.jar");

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 如果输出路径存在，则删除
//        Path output = new Path("/output");
//        FileSystem fs = FileSystem.get(new URI("hdfs://hadoop101:8020"), conf, "hadoop");
//        if (fs.exists(output)) {
//            fs.delete(output, true);
//        }
//
//        fs.close();

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("/input"));
        FileOutputFormat.setOutputPath(job, new Path("/output"));// 注意：输出路径必须不存在

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        // 非必要的，程序退出
        System.exit(res ? 0 : -1);
    }
}
