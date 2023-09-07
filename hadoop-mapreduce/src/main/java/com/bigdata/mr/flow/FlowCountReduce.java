package com.bigdata.mr.flow;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

/**
 * Date:2023/9/6
 * Author:wfm
 * Desc:
 */
public class FlowCountReduce extends Reducer<Text,FlowBean,Text,FlowBean> {


    @Override
    protected void reduce(Text key, Iterable<FlowBean> values, Context context) throws IOException, InterruptedException {
        // 上行流量和下行流量
        int upFlow = 0;
        int dFlow = 0;

        // 遍历一组手机号码的所有的value值
        Iterator<FlowBean> iterator = values.iterator();
        while (iterator.hasNext()){
            FlowBean flowBean = iterator.next();
            upFlow += flowBean.getUpFlow();
            dFlow += flowBean.getdFlow();
        }

        // 输出
        context.write(key,new FlowBean(key.toString(),upFlow,dFlow));
    }
}
