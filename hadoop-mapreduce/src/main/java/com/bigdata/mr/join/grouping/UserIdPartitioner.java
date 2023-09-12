package com.bigdata.mr.join.grouping;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:
 */
public class UserIdPartitioner extends Partitioner<OrderUserBean, NullWritable> {
    @Override
    public int getPartition(OrderUserBean key, NullWritable value, int numReduceTasks) {
        return (key.getUserId().hashCode() & Integer.MAX_VALUE) % numReduceTasks;
    }
}
