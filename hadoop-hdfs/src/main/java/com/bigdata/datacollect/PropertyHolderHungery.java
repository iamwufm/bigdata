package com.bigdata.datacollect;

import java.io.IOException;
import java.util.Properties;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:
 * 单例设计模式，方式一： 饿汉式单例.
 * 加载类就加载，不需要调用方法
 */
public class PropertyHolderHungery {
    private static Properties prop = new Properties();

    static {
        try {
            prop.load(PropertyHolderHungery.class.getClassLoader().getResourceAsStream("collect.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Properties getProps() throws Exception{
        return prop;
    }
}
