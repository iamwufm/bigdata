package com.bigdata.test;



import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:
 */
public class Demo1 {
    public static void main(String[] args) throws IOException {
        // 判断输出目录是否存在，存在删除
        File output = new File("C:\\Alearning\\data\\mr\\wc\\output");
        if (output.exists()){
            System.out.println("hahah");
            FileUtils.deleteDirectory(output);
        }
    }
}
