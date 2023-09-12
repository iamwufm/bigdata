package com.bigdata.mr.join.grouping;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Date:2023/9/12
 * Author:wfm
 * Desc:
 */
public class OrderUserBean implements WritableComparable<OrderUserBean> {
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
    @Override
    public int compareTo(OrderUserBean o) {
        // 按用户id升序排序，如果一样，按tableName降序(tableName值为order和user)
        if (this.userId.compareTo(o.userId) == 0){
            return o.tableName.compareTo(this.tableName);
        }
        return this.userId.compareTo(o.userId);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(orderId);
        dataOutput.writeUTF(userId);
        dataOutput.writeUTF(userName);
        dataOutput.writeInt(userAge);
        dataOutput.writeUTF(userFriend);
        dataOutput.writeUTF(tableName);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        orderId = dataInput.readUTF();
        userId = dataInput.readUTF();
        userName = dataInput.readUTF();
        userAge = dataInput.readInt();
        userFriend = dataInput.readUTF();
        tableName = dataInput.readUTF();
    }

    public void set(String orderId, String userId, String userName, int userAge, String userFriend, String tableName) {
        this.orderId = orderId;
        this.userId = userId;
        this.userName = userName;
        this.userAge = userAge;
        this.userFriend = userFriend;
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

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return this.orderId + "," + this.userId + "," + this.userAge + "," + this.userName + "," + this.userFriend;
    }

}
