package com.bigdata.datacollect;

import java.util.Timer;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:数据收集主入口
 *
 * 项目详细描述见resources下的datacollect_plan.txt
 */
public class DataCollectMain {

    public static void main(String[] args) {

        // 多线程定时器（1和2多线程执行）
        Timer timer = new Timer();

        // 1.每隔一小时执行--将日志文件采集到hdfs
        // 1秒=1000毫秒，第一个参数是任务，第二个参数是延迟多久，第三个参数是每隔多久
        timer.schedule(new CollectTask(),0,60*60*1000L);

        // 2. 每隔一小时执行--将备超过规定时间的份文件删除
        timer.schedule(new BackupCleanTask(),0,60*60*1000L);
    }
}
