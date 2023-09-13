package com.bigdata.mr.order.grouping;

import org.apache.hadoop.io.WritableComparable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Date:2023/9/11
 * Author:wfm
 * Desc:
 */
public class OrderBean implements WritableComparable<OrderBean> {
    // 订单id
    private String orderId;
    // 用户id
    private String userId;
    // 产品名称
    private String pdtName;
    // 单价
    private float price;
    // 数量
    private int number;
    // 销售额
    private float amountFee;

    @Override
    public String toString() {
        return orderId + "," + userId + "," + pdtName + "," + price + "," + number + "," + amountFee;
    }

    public void set(String orderId, String userId, String pdtName, float price, int number) {
        this.orderId = orderId;
        this.userId = userId;
        this.pdtName = pdtName;
        this.price = price;
        this.number = number;
        this.amountFee = price * number;
    }

    @Override
    public int compareTo(OrderBean o) {
        // 按订单id升序排序，相同订单id按销售额降序
        if (this.orderId.compareTo(o.orderId) == 0) {

            return Float.compare(o.amountFee, this.amountFee);
        }
        return this.orderId.compareTo(o.orderId);
    }

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(orderId);
        dataOutput.writeUTF(userId);
        dataOutput.writeUTF(pdtName);
        dataOutput.writeFloat(price);
        dataOutput.writeInt(number);
        dataOutput.writeFloat(amountFee);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        orderId = dataInput.readUTF();
        userId = dataInput.readUTF();
        pdtName = dataInput.readUTF();
        price = dataInput.readFloat();
        number = dataInput.readInt();
        amountFee = dataInput.readFloat();
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

    public String getPdtName() {
        return pdtName;
    }

    public void setPdtName(String pdtName) {
        this.pdtName = pdtName;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public float getAmountFee() {
        return amountFee;
    }

    public void setAmountFee(float amountFee) {
        this.amountFee = amountFee;
    }
}
