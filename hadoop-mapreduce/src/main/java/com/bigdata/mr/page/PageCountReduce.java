package com.bigdata.mr.page;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:reduce阶段
 * 对网址进行统计
 * 网址,总访问数
 * 最后取前三
 */
public class PageCountReduce extends Reducer<Text, IntWritable, Text, IntWritable> {

    TreeMap<PageBean, Object> treeMap = new TreeMap<>();
    Text k = new Text();
    IntWritable v = new IntWritable();

    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int pageCount = 0;

        // 遍历一组key的所有value值，把value值相加在一起
        Iterator<IntWritable> iterator = values.iterator();
        while (iterator.hasNext()) {
            pageCount += iterator.next().get();
        }

        PageBean pageBean = new PageBean();
        pageBean.set(key.toString(), pageCount);

        // 放入treemap，排序规则写在pageBean里
        treeMap.put(pageBean, null);
    }

    // 执行完reduce后再执行
    @Override
    protected void cleanup(Context context) throws IOException, InterruptedException {
        int i = 0;

        // 遍历treeMap
        Set<PageBean> pageBeans = treeMap.keySet();

        for (PageBean pageBean : pageBeans) {
            k.set(pageBean.getPage());
            v.set(pageBean.getCount());
            context.write(k,v);
            i++;

            // 遍历前三个就结束
            if (i == 3) return;
        }
    }
}
