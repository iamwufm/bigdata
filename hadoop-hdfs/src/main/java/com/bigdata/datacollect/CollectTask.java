package com.bigdata.datacollect;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:日志文件采集到hdfs
 * 实现步骤：
 * 1.探测日志源目录
 * 2.获取需要采集地文件
 * 3.移动这些文件到一个待上传的临时目录
 * 4.遍历待上传目录中的文件，逐一传输到HDFS
 * 5.上传成功后，将文件移动到备份目录
 */
public class CollectTask extends TimerTask {

    @Override
    public void run() {
        try {
            // 获取配置参数
            final Properties props = PropertyHolderLazy.getProps();
            // 获取本次采集时的日期
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH");
            String day = sdf.format(new Date());
            // 日志源路径
            File srcDir = new File(props.getProperty(Constants.LOG_SOURCE_DIR));
            //待上传临时目录
            File toUploadDir = new File(props.getProperty(Constants.LOG_TOUPLOAD_DIR));
            // 备份目录
            File backupDir = new File(props.getProperty(Constants.LOG_BACKUP_BASE_DIR) + "/" + day);

            // HDFS存储路径
            Path hdfsDestPath = new Path(props.getProperty(Constants.HDFS_DEST_BASE_DIR) + "/" + day);

            // 会加载resource目录下log4j.properties
            Logger logger = Logger.getLogger("logRollingFile");


            // 1.探测日志源目录
            File[] srcFiles = srcDir.listFiles(new FilenameFilter() {
                // 2.获取需要采集地文件
                public boolean accept(File dir, String name) {
                    // 把access.log剔除掉，因为这个是正在写的日志文件
                    if (name.startsWith(props.getProperty(Constants.LOG_LEGAL_PREFIX))) {
                        return true;
                    }
                    return false;
                }
            });

            // 记录日志
            logger.info("探测到如下文件需要采集：" + Arrays.toString(srcFiles));

            // 3.移动这些文件到一个待上传的临时目录
            for (File file : srcFiles) {

                FileUtils.moveFileToDirectory(file, toUploadDir, true);
            }

            // 记录日志
            logger.info("上述文件移动到了待上传目录" + toUploadDir.getAbsolutePath());

            // 4.遍历待上传目录中的文件，逐一传输到HDFS
            // 4.1创建hdfs客户端
            FileSystem fs = FileSystem.get(new URI(props.getProperty(Constants.HDFS_URI)), new Configuration(), "hadoop");

            // 4.2检查hfds上的目录是否存在,不存在则创建
            if (!fs.exists(hdfsDestPath)) {
                fs.mkdirs(hdfsDestPath);
            }

            // 4.3遍历待上传目录中的文件，逐一传输到HDFS
            for (File toUploadFile : toUploadDir.listFiles()) {
                // 把文件上传到hdfs
                Path destPath = new Path(hdfsDestPath + "/" + props.getProperty(Constants.HDFS_FILE_PREFIX)+ UUID.randomUUID() + props.getProperty(Constants.HDFS_FILE_SUFFIX));
                fs.copyFromLocalFile(new Path(toUploadFile.getAbsolutePath()), destPath);

                // 记录日志
                logger.info("文件传输到HDFS完成：" + toUploadFile.getAbsolutePath() + "-->" + destPath);

                // 5.上传成功后，将文件移动到备份目录
                FileUtils.moveFileToDirectory(toUploadFile, backupDir, true);

                // 记录日志
                logger.info("文件备份完成：" + toUploadFile.getAbsolutePath() + "-->" + backupDir);
            }

            // 关闭资源
            fs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
