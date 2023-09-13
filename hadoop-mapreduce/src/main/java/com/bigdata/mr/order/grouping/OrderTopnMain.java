package com.bigdata.mr.order.grouping;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/11
 * Author:wfm
 * Desc:展示每个订单商品销售额前三的订单详情
 * <p>
 * 1.输入数据
 * order001,u001,小米6,1999.9,2
 * order001,u001,雀巢咖啡,99.0,2
 * order001,u001,安慕希,250.0,2
 * order001,u001,经典红双喜,200.0,4
 * order001,u001,防水电脑包,400.0,2
 * <p>
 * 2.输出数据
 * order001,u001,小米6,1999.9,2
 * order001,u001,防水电脑包,400.0,2
 * order001,u001,安慕希,250.0,2
 */
public class OrderTopnMain {
    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();// 默认只加载core-default.xml core-site.xml
        conf.setInt("order.top.n", 3);
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(OrderTopnMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(OrderTopnMapper.class);
        job.setReducerClass(OrderTopnReducer.class);

//        job.setInputFormatClass();

        // 自定义分区和GroupingComparator
        job.setPartitionerClass(OrderIdPartitioner.class);
        job.setGroupingComparatorClass(OrderIdGroupingComparator.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(OrderBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(OrderBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\order\\output1");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }


        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\order\\input"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\order\\output1"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);
    }

    private static class OrderTopnMapper extends Mapper<LongWritable, Text, OrderBean, NullWritable> {

        OrderBean k = new OrderBean();
        NullWritable v = NullWritable.get();

        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, OrderBean, NullWritable>.Context context) throws IOException, InterruptedException {
            // 处理每一行数据
            String[] words = value.toString().split(",");
            k.set(words[0], words[1], words[2], Float.parseFloat(words[3]), Integer.parseInt(words[4]));

            context.write(k, v);

        }
    }

    private static class OrderTopnReducer extends Reducer<OrderBean, NullWritable, OrderBean, NullWritable> {
        int topn;
        NullWritable v = NullWritable.get();

        @Override
        protected void setup(Reducer<OrderBean, NullWritable, OrderBean, NullWritable>.Context context) throws IOException, InterruptedException {
            Configuration conf = context.getConfiguration();
            topn = conf.getInt("order.top.n", 3);

        }

        @Override
        protected void reduce(OrderBean key, Iterable<NullWritable> values, Reducer<OrderBean, NullWritable, OrderBean, NullWritable>.Context context) throws IOException, InterruptedException {
            int count = 0;
            // 虽然reduce方法中的参数key只有一个，但是只要迭代器迭代一次，key中的值就会变
            for (NullWritable value : values) {
                context.write(key, v);
                count++;
                if (count == topn) return;
            }
        }
    }
}
