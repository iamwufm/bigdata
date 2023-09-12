package com.bigdata.mr;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:
 */
public class Demo {
    public static void main(String[] args) {
        String line = "u001,senge,18,angelababy";
        int index = line.indexOf(",");

        System.out.println(index);

        System.out.println(line.substring(index+1));
        System.out.println(line.substring(0,index));
    }
}
