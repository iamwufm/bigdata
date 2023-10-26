package com.bigdata.phoenix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Properties;

/**
 * Date:2023/10/23
 * Author:wfm
 * Desc: 通过phoenix查询hbase数据
 */
public class PhoenixClient {
    public static void main(String[] args) throws Exception {
        // 没有需要添加的必要配置 因为 Phoenix 没有账号密码
        Properties properties = new Properties();
        properties.setProperty("phoenix.schema.isNamespaceMappingEnabled", "true");
        properties.setProperty("phoenix.schema.mapSystemTablesToNamespace", "true");

        Connection connection = DriverManager.getConnection("jdbc:phoenix:hadoop101,hadoop102,hadoop103:2181", properties);
        PreparedStatement prp = connection.prepareStatement("select * from students limit 5");
        ResultSet resultSet = prp.executeQuery();

        while (resultSet.next()) {
            String pk = resultSet.getString("pk");
            String name = resultSet.getString("name");
            long age = resultSet.getLong("age");

            System.out.println(pk + "----" + name + "----" + age);
        }

        // 关闭资源
        connection.close();

    }
}
