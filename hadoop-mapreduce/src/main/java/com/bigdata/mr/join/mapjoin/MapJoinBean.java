package com.bigdata.mr.join.mapjoin;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:
 */
public class MapJoinBean {
    // 订单id
    private String orderId;
    // 用户id
    private String userId;
    // 用户名
    private String userName;
    // 年龄
    private int userAge;
    // 用户的朋友
    private String userFriend;
    // 表名
    private String tableName;

    public void set(String orderId, String userId, String userName, int userAge, String userFriend, String tableName) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userFriend = userFriend;
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return this.orderId + "," + this.userId + "," + this.userAge + "," + this.userName + "," + this.userFriend;
    }

}
