package com.bigdata.mr.flow;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Date:2023/9/6
 * Author:wfm
 * Desc: map阶段
 */
public class FlowCountMapper extends Mapper<LongWritable, Text, Text, FlowBean> {

    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 获取一行数据并切分
        String[] words = value.toString().split("\t");

        // 获取电话号码，上行流量和下行流量
        String phoneNum = words[1];
        int upFlow = Integer.parseInt(words[words.length - 3]);
        int dFlow = Integer.parseInt(words[words.length - 2]);


        // 输出
        context.write(new Text(phoneNum),new FlowBean(phoneNum, upFlow, dFlow));


    }
}
