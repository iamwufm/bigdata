package com.bigdata.mr.page;

import org.apache.hadoop.conf.Configuration;

/**
 * Date:2023/9/7
 * Author:wfm
 * Desc:
 */
public class PageTopNTest {
    public static void main(String[] args) {
        Configuration conf = new Configuration();
//        conf.set("topn","3");
        conf.addResource("test.xml");

        System.out.println(conf.get("topn"));
    }
}
