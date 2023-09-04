package com.bigdata.test;

import java.io.File;
import java.util.Date;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:
 */
public class Demo1 {
    public static void main(String[] args) {
        File file = new File("C:/Alearning/data/logs/backup");

        long nowTime = new Date().getTime();
        System.out.println(nowTime);

        for (File listFile : file.listFiles()) {
            System.out.println(listFile);
            System.out.println(listFile.getName());
        }
    }
}
