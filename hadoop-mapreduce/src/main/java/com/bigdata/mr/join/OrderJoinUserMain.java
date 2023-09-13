package com.bigdata.mr.join;

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
import java.util.ArrayList;

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
 * 本例是使用最low的方式实现
 * 还可以利用Partitioner+CompareTo+GroupingComparator 组合拳来高效实现
 */
public class OrderJoinUserMain {
    public static void main(String[] args) throws Exception {

        // 1.创建job对象
        Configuration conf = new Configuration();
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(OrderJoinUserMain.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(OrderJoinUserMapper.class);
        job.setReducerClass(OrderJoinUserReduce.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(JoinBean.class);

        job.setOutputKeyClass(JoinBean.class);
        job.setOutputValueClass(NullWritable.class);

        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\join\\output");
        if (output.exists()) {
            FileUtils.deleteDirectory(output);
        }

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("C:\\Alearning\\data\\mr\\join\\input"));
        FileOutputFormat.setOutputPath(job, new Path("C:\\Alearning\\data\\mr\\join\\output"));

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        boolean res = job.waitForCompletion(true);

        System.exit(res ? 0 : -1);
    }

    static class OrderJoinUserMapper extends Mapper<LongWritable, Text, Text, JoinBean> {
        String fileName;
        Text k = new Text();
        JoinBean v = new JoinBean();

        @Override
        protected void setup(Mapper<LongWritable, Text, Text, JoinBean>.Context context) throws IOException, InterruptedException {
            // 获取文件名
            FileSplit inputSplit = (FileSplit) context.getInputSplit();
            fileName = inputSplit.getPath().getName();
        }

        // 输出<用户id,bean对象>
        @Override
        protected void map(LongWritable key, Text value, Mapper<LongWritable, Text, Text, JoinBean>.Context context) throws IOException, InterruptedException {
            // 处理一行数据
            String[] words = value.toString().split(",");
            if (fileName.startsWith("order")) {
                v.set(words[0], words[1], "NULL", -1, "NULL", "order");
                k.set(words[1]);
            } else {
                v.set("NULL", words[0], words[1], Integer.parseInt(words[2]), words[3], "user");
                k.set(words[0]);
            }

            context.write(k, v);
        }

    }

    static class OrderJoinUserReduce extends Reducer<Text, JoinBean, JoinBean, NullWritable> {
        NullWritable v = NullWritable.get();

        // 输入<用户id,bean对象>,输出<bean对象,null>
        @Override
        protected void reduce(Text key, Iterable<JoinBean> values, Reducer<Text, JoinBean, JoinBean, NullWritable>.Context context) throws IOException, InterruptedException {
            ArrayList<JoinBean> orderList = new ArrayList<>();
            JoinBean userBean = null;

            try {
                // 区别两类数据
                for (JoinBean value : values) {
                    // 如果是order表的数据，添加到orderList集合
                    if (value.getTableName().equals("order")) {
                        JoinBean orderBean = new JoinBean();
                        BeanUtils.copyProperties(orderBean, value);
                        orderList.add(orderBean);
                    } else {
                        userBean = new JoinBean();
                        BeanUtils.copyProperties(userBean, value);
                    }
                }

                // 拼接数据，并输出
                for (JoinBean k : orderList) {
                    k.setUserName(userBean.getUserName());
                    k.setUserAge(userBean.getUserAge());
                    k.setUserFriend(userBean.getUserFriend());

                    // 写数据
                    context.write(k, v);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }
}
