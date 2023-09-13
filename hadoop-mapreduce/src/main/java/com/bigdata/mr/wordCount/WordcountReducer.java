package com.bigdata.mr.wordCount;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Date:2023/9/5
 * Author:wfm
 * Desc:
 */

public class WordcountReducer extends Reducer<Text, IntWritable, Text, IntWritable> {

    IntWritable v = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {

        int count = 0;
        // 对相同key的value值进行相加
        Iterator<IntWritable> iterator = values.iterator();

        while (iterator.hasNext()) {
            IntWritable value = iterator.next();
            count += value.get();
        }
        v.set(count);
        context.write(key, v);

    }
}
