package com.bigdata.mr.join.grouping;

import org.apache.commons.beanutils.BeanUtils;
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
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:订单数据关联用户数据。类似的sql：`select a.*,b.* from a join b on a.uid=b.uid;`
 * 1.输入数据
 * # 订单数据
 * order001,u001
 * order002,u001
 * order003,u005
 * <p>
 * #用户数据
 * u001,senge,18,angelababy
 * u002,laozhao,48,ruhua
 * u003,xiaoxu,16,chunge
 * <p>
 * 2.输出数据
 * order001,u001,senge,18,angelababy
 * order002,u001,laozhao,48,ruhua
 * order003,u003,xiaoxu,16,chunge
 * <p>
 * 利用Partitioner+CompareTo+GroupingComparator 组合拳来高效实现
 */
public class UserGroupingMain {
    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();// 默认只加载core-default.xml core-site.xml
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(UserGroupingMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(UserGroupingMapper.class);
        job.setReducerClass(UserGroupingReduce.class);

//        job.setInputFormatClass();

        // 自定义分区和GroupingComparator
        job.setPartitionerClass(UserIdPartitioner.class);
        job.setGroupingComparatorClass(UserIdGroupingComparator.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(OrderUserBean.class);
        job.setMapOutputValueClass(NullWritable.class);

        job.setOutputKeyClass(OrderUserBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\join\\output2");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }


        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\join\\input"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\join\\output2"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);
    }

    static class UserGroupingMapper extends Mapper<LongWritable, Text, OrderUserBean, NullWritable> {
        private String fileName;
        private OrderUserBean k = new OrderUserBean();
        private NullWritable v = NullWritable.get();

        @Override
        protected void setup(Mapper<LongWritable, Text, OrderUserBean, NullWritable>.Context context) throws IOException, InterruptedException {
            // 获取输入文件名称
            FileSplit inputSplit = (FileSplit) context.getInputSplit();
            fileName = inputSplit.getPath().getName();
        }

        // 输出<bean对象，null>
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, OrderUserBean, NullWritable>.Context context) throws IOException, InterruptedException {
            // 处理一行数据
            String[] words = value.toString().split(",");

            // 如果来自order表
            if (fileName.startsWith("order")) {
                k.set(words[0], words[1], "NULL", -1, "NULL", "order");
            } else {
                k.set("NULL", words[0], words[1], Integer.parseInt(words[2]), words[3], "user");
            }

            context.write(k, v);
        }
    }

    static class UserGroupingReduce extends Reducer<OrderUserBean, NullWritable, OrderUserBean, NullWritable> {
        private NullWritable v = NullWritable.get();

        //  输入<bean对象，null>，输出<bean对象，null>
        @Override
        protected void reduce(OrderUserBean key, Iterable<NullWritable> values, Reducer<OrderUserBean, NullWritable, OrderUserBean, NullWritable>.Context context) throws IOException, InterruptedException {
            OrderUserBean userBean = new OrderUserBean();
            // 处理一组key
            // 虽然key只有一个，但是每迭代一次values,key会变化
            // 第一次迭代value，key值是user
            try {
                for (NullWritable value : values) {
                    if (key.getTableName().equals("user")) {
                        BeanUtils.copyProperties(userBean, key);
                    } else {
                        key.setUserName(userBean.getUserName());
                        key.setUserAge(userBean.getUserAge());
                        key.setUserFriend(userBean.getUserFriend());
                        // 写数据
                        context.write(key, v);
                    }
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
