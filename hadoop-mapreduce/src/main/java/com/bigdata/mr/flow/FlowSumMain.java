package com.bigdata.mr.flow;

import com.bigdata.mr.wordCount.JobSubmitterWindowsLocal;
import com.bigdata.mr.wordCount.WordcountMapper;
import com.bigdata.mr.wordCount.WordcountReducer;
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
 * Date:2023/9/6
 * Author:wfm
 * Desc:统计每一个手机号耗费的总上行流量、总下行流量、总流量
 *
 * 输入数据格式：
 * id   手机号码   ... 上行流量 下行流量 网络状态码
 * 1	18320173382	...	9531	2412	200
 * 输出数据格式：
 * 13560436666 1116,954,2070
 * 手机号码 上行流量 下行流量 总流量
 *
 * 实现方式：
 * 在map阶段
 * 输出：手机号码，对象
 * 在reduce阶段：手机号码，对象
 */
public class FlowSumMain {

    public static void main(String[] args) throws Exception{
        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(FlowSumMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(FlowCountMapper.class);
        job.setReducerClass(FlowCountReduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(FlowBean.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(FlowBean.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\flow\\output1");
        if (output.exists()){
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job,new Path("C:\\Alearning\\data\\mr\\flow\\input"));
        FileOutputFormat.setOutputPath(job,new Path("C:\\Alearning\\data\\mr\\flow\\output1"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);
    }
}
