package com.bigdata.datacollect;

import java.util.Properties;

/**
 * Date:2023/9/2
 * Author:wfm
 * Desc:
 * 单例模式：懒汉式——考虑了线程安全
 * <p>
 * 调用方法才加载，只需要调用一次方法即可
 */
public class PropertyHolderLazy {

    private static Properties prop = null;

    public static Properties getProps() throws Exception {
        if (prop == null) {
            // 加锁，只能一个进入，后面排队
            synchronized (PropertyHolderLazy.class) {
                if (prop == null) {
                    prop = new Properties();
                    prop.load(PropertyHolderHungery.class.getClassLoader().getResourceAsStream("collect.properties"));
                }
            }
        }

        return prop;
    }
}
