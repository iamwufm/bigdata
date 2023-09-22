package com.bigdata.test;



import org.apache.commons.io.FileUtils;
import org.apache.directory.api.util.OsgiUtils;

import java.io.File;
import java.io.IOException;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:
 */
public class Demo1 {
    public static void main(String[] args) throws IOException {
        int num1 = 2;
        System.out.println("hello java");
        int num2 = 4;
        System.out.println("hello python");

        // 调用方法
        int sum = getSum(num1, num2);
        System.out.println(sum);
    }

    public static int getSum(int num1,int num2){
        System.out.println("hello shell");
        System.out.println("hello hadoop");
        return num1 + num2;
    }
}
