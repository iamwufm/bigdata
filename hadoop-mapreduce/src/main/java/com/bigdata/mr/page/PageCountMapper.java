package com.bigdata.mr.page;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:map阶段
 *
 * 输出：
 * 网址,1
 */
public class PageCountMapper extends Mapper<LongWritable, Text, Text, IntWritable> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // 切分一行数据
        String[] words = value.toString().split(" ");

        context.write(new Text(words[1]),new IntWritable(1));
    }
}
