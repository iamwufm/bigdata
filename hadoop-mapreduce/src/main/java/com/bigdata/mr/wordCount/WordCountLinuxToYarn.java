package com.bigdata.mr.wordCount;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import java.util.Arrays;

/**
 * Date:2023/9/5
 * Author:wfm
 * Desc:单词统计主主入口
 * 以空格切分，统计单词出现次数
 * 准备：
 * 在hdfs的/input准备几份数据
 * <p>
 * 程序写完记得打包 package，然后把包放在集群中的任意机器中，最后执行
 * hadoop jar 包名.jar JobSubmitterLinuxToYarn
 * 或者
 * hadoop jar 包名.jar JobSubmitterLinuxToYarn 参数1 参数2 ...
 * 如果要在hadoop集群的某台机器上启动这个job提交客户端的话
 * conf里面就不需要指定fs.defaultFS  mapreduce.framework.name等
 * <p>
 * 因为在集群上用hadoop jar xx.jar JobSubmitterLinuxToYarn
 * 命令来启动客户端main方法时
 * hadoop jar这个命令会将所在机器上的hadoop安装目录中的jar包和配置文件加入到运行时的classpath中
 * <p>
 * 那么我们在客户端main方法中的new Configuration()语句就会加载classpath中的配置文件，自然就有了
 * fs.defaultFS  mapreduce.framework.name等这些参数配置
 */
public class WordCountLinuxToYarn implements Tool {
    private Configuration conf;
    private static Tool tool;

    public static void main(String[] args) throws Exception {
        // 1. 创建配置文件
        Configuration conf1 = new Configuration();
        // 2. 判断是否有 tool 接口
        switch (args[0]) {
            case "wordcount":
                tool = new WordCountLinuxToYarn();
                break;
            default:
                throw new RuntimeException(" No such tool: " +
                        args[0]);
        }
        // 3. 用 Tool 执行程序
        // Arrays.copyOfRange 将老数组的元素放到新数组里面
        int run = ToolRunner.run(conf1, tool,
                Arrays.copyOfRange(args, 1, args.length));
        System.exit(run);

    }

    @Override
    public int run(String[] strings) throws Exception {
        // 1.创建job对象
        Job job = Job.getInstance(conf);

        // 2.封装参数
        // 2.1 jar包所在位置
        job.setJarByClass(WordCountLinuxToYarn.class);

        // 2.2 本次job所要调用的mapper实现类、reduce实现类
        job.setMapperClass(WordcountMapper.class);
        job.setReducerClass(WordcountReducer.class);

        // 2.3 本次job的mapper实现类、reduce实现类产生的结果数据的key、value类型
        job.setMapOutputKeyClass(Text.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        // 2.4 本次job要处理的输入数据集所在路径、最终结果的输出路径
        FileInputFormat.setInputPaths(job, new Path("/input"));
        FileOutputFormat.setOutputPath(job, new Path("/output"));// 注意：输出路径必须不存在

        // 2.5 想要启动的reduce task的数量
        job.setNumReduceTasks(2);

        // 3.提交job给yarn
        return job.waitForCompletion(true) ? 0 : 1;
    }

    @Override
    public void setConf(Configuration conf) {
        this.conf = conf;
    }

    @Override
    public Configuration getConf() {
        return conf;
    }
}
