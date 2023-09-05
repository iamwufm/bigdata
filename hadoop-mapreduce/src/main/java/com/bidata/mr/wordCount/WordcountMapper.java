package com.bidata.mr.wordCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

/**
 * Date:2023/9/5
 * Author:wfm
 * Desc: 单词统计的map阶段逻辑
 * 每行以空格切开，输出结果：单词,1
 *
 * Mapper<KEYIN, VALUEIN, KEYOUT, VALUEOUT>
 * KEYIN：是map task读取到的数据的key的类型，是一行的起始偏移量Long
 * VALUEIN：是map task读取到的数据的value的类型，是一行的内容String
 *
 */
public class WordcountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    // 重写方法快捷键ctrl+o
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        super.map(key, value, context);
    }
}

