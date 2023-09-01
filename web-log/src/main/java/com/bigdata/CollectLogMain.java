package com.bigdata;
import org.apache.log4j.Logger;

/**
 * Date:2023/8/31
 * Author:wfm
 * Desc:模拟收集web页面日志。
 *
 * 按要求生成日志，把日志写入access.log文件（存放路径：C:/Alearning/data/log）
 * 64k滚动一个文件，最多保存200个文件
 *
 */
public class CollectLogMain {

    public static void main(String[] args) throws Exception{

        // 会加载resource目录下log4j.properties
        Logger logger = Logger.getLogger("logRollingFile");

        while (true){

            // 生成日志，并写入规定的文件中
            logger.info("1234567890----"+ System.currentTimeMillis());
            // 休眠50毫秒
            Thread.sleep(50);
        }


    }
}
