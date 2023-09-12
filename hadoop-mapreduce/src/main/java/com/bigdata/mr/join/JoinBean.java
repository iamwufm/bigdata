package com.bigdata.mr.join;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:
 */
public class JoinBean implements Writable {
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserAge(int userAge) {
        this.userAge = userAge;
    }

    public String getUserFriend() {
        return userFriend;
    }

    public void setUserFriend(String userFriend) {
        this.userFriend = userFriend;
    }

    @Override
    public String toString() {
        return this.orderId + "," + this.userId + "," + this.userAge + "," + this.userName + "," + this.userFriend;
    }

    @Override
    public void write(DataOutput out) throws IOException {
        out.writeUTF(this.orderId);
        out.writeUTF(this.userId);
        out.writeUTF(this.userName);
        out.writeInt(this.userAge);
        out.writeUTF(this.userFriend);
        out.writeUTF(this.tableName);

    }

    @Override
    public void readFields(DataInput in) throws IOException {
        this.orderId = in.readUTF();
        this.userId = in.readUTF();
        this.userName = in.readUTF();
        this.userAge = in.readInt();
        this.userFriend = in.readUTF();
        this.tableName = in.readUTF();

    }

}

