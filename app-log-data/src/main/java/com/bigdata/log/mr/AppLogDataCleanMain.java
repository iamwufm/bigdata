package com.bigdata.log.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.LazyOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

/**
 * Date:2023/10/16
 * Author:wfm
 * Desc:
 *
 *  hadoop jar appLogDataClean-jar-with-dependencies.jar /app-log-data/data/20170814 /app-log-data/clean/20170814
 *  hadoop jar 包名.jar  参数1 参数2(在pom文件指定了主类。可以不用写类名))
 */
public class AppLogDataCleanMain {
    public static void main(String[] args) throws Exception{
        Configuration conf = new Configuration();

        Job job = Job.getInstance(conf);

        job.setJarByClass(AppLogDataCleanMain.class);

        job.setMapperClass(AppLogDataCleanMapper.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(NullWritable.class);

        job.setNumReduceTasks(0);

        // 避免生成默认的part-m-00000等文件，因为，数据已经交给MultipleOutputs输出了
        LazyOutputFormat.setOutputFormatClass(job, TextOutputFormat.class);

        // 在windows上：C:\Alearning\data\app-log-data\input\20170814 C:\Alearning\data\app-log-data\output
        // 在linux上：/app-log-data/data/20170814 /app-log-data/clean/20170814
        FileInputFormat.setInputPaths(job, new Path(args[0]));

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        boolean res = job.waitForCompletion(true);
        System.exit(res ? 0 : 1);
    }
}
