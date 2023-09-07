package com.bigdata.mr.wordCount;

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
 * KEYOUT：是用户的自定义map方法要返回的结果kv数据的key类型，在wordcount逻辑中，我们需要返回单词的数据类型String
 * VALUEOUT：是用户的自定义map方法要返回的结果kv数据的value类型,在wordcount逻辑中,我们返回的是整数Integer
 *
 * 在mapreduce中，map产生的数据需要传输给reduce,需要进行序列化和反序列化，而jdk中原生序列化机制产生的数据量比较冗余，
 * 运行过程中传输效率低下
 * 所以，hadoop专门设计了自己的序列化机制。mapreduce中传输数据类型就必须实现hadoop自己的序列化接口
 *
 * hadoop为jdk的常用基本类型Long String Integer Float等数据类型封装了自己实现hadoop序列化接口的的类型
 * LongWritable Text IntWritable FloatWritable
 *
 */
public class WordcountMapper extends Mapper<LongWritable, Text,Text, IntWritable> {

    // 重写方法快捷键ctrl+o
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        // 切单词
        String line = value.toString();
        String[] words = line.split(" ");

        for (String word : words) {
            context.write(new Text(word),new IntWritable(1));
        }
    }
}

