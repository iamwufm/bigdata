package com.bigdata.mr.friends;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Date:2023/9/13
 * Author:wfm
 * Desc:求共同好友
 * <p>
 * 1.输入数据
 * 用户id:好友
 * A:B,C,D,F,E,O
 * B:A,C,E,K
 * <p>
 * 2.输出数据
 * A-B:C
 * A-B:E
 */
public class CommonFriends1Main {
    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(CommonFriends1Main.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(CommonFriends1Mapper.class);
        job.setReducerClass(CommonFriends1Reduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(Text.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\friends\\output");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\friends\\input"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\friends\\output"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(1);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }

    static class CommonFriends1Mapper extends Mapper<LongWritable, Text, Text, Text> {
        Text k = new Text();
        Text v = new Text();

        // 输出<好友，用户>
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            // 处理一行数据 A:B,C,D,F,E,O
            String[] words = value.toString().split(":");
            v.set(words[0]);

            for (String friend : words[1].split(",")) {
                k.set(friend);
                context.write(k, v);
            }
        }
    }

    static class CommonFriends1Reduce extends Reducer<Text, Text, Text, Text> {
        Text k = new Text();

        // 输入<好友，用户>，输出<"用户-用户",好友>
        @Override
        protected void reduce(Text key, Iterable<Text> values, Reducer<Text, Text, Text, Text>.Context context) throws IOException, InterruptedException {
            ArrayList<String> arrayList = new ArrayList<>();

            // 处理一组数据
            for (Text user : values) {
                arrayList.add(user.toString());
            }

            // 对集合进行排序
            Collections.sort(arrayList);

            // 嵌套遍历集合
            for (int i = 0; i < arrayList.size() - 1; i++) {
                for (int j = i + 1; j < arrayList.size(); j++) {
                    k.set(arrayList.get(i) + "-" + arrayList.get(j));
                    context.write(k, key);
                }
            }


        }
    }
}
