package com.bigdata.mr.flow;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

import java.util.HashMap;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:MapTask通过这个类的getPartition方法，来计算它所产生的每一对kv数据该分发给哪一个reduce task
 * <p>
 * 本类是提供给MapTask用的
 * <p>
 * 如果在getPartition方法写从数据库获取手机归属地，每个kv都需要调用数据库，这个方法会造成数据库的大量连接
 * 建议在用单例模式，在创建类时从数据库获取一次就可以，用静态代码块的方法
 * 加载类就加载，不需要调用方法
 */
public class ProvincePartitioner extends Partitioner<Text, FlowBean> {

    static HashMap<String, Integer> codeMap = new HashMap<>();

    static {
        codeMap.put("135", 0);
        codeMap.put("136", 1);
        codeMap.put("137", 2);
        codeMap.put("138", 3);
        codeMap.put("139", 4);
    }

    @Override
    public int getPartition(Text key, FlowBean value, int numPartitions) {
        Integer code = codeMap.get(key.toString().substring(0, 3));
        return code == null ? 5 : code;
    }

}
