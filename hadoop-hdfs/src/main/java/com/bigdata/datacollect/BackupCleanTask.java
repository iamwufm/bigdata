package com.bigdata.datacollect;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.TimerTask;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:将备超过规定时间的份文件删除
 * <p>
 * 探测备份目录中的备份数据，检查是否超过最长备份时长，如果超过，则删除
 */
public class BackupCleanTask extends TimerTask {

    @Override
    public void run() {
        try {
            // 获取配置参数
            final Properties props = PropertyHolderLazy.getProps();
            // 备份文件目录
            File files = new File(props.getProperty(Constants.LOG_BACKUP_BASE_DIR));
            // 超时时间
            int timeout = Integer.parseInt(props.getProperty(Constants.LOG_BACKUP_TIMEOUT));


            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            // 当前时间戳
            long nowTime = new Date().getTime();

            // 遍历备份文件
            for (File dir : files.listFiles()) {
                // 获取目录名称,并把yyyy-mm-dd-HH转为时间戳
                long dirTime = sdf.parse(dir.getName()).getTime();
                // 如果目录时间超过24小时，则删除文件夹
                if (nowTime - dirTime >= timeout * 60 * 60 * 1000L) {
                    FileUtils.deleteDirectory(dir);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}