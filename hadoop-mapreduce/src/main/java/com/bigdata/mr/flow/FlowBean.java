package com.bigdata.mr.flow;

import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

/**
 * Date:2023/9/6
 * Author:wfm
 * Desc: 封装手机号码信息
 * <p>
 * 本案例的功能：演示自定义数据类型如何实现hadoop的序列化接口
 * 1、该类一定要保留空参构造函数
 * 2、write方法中输出字段二进制数据的顺序  要与  readFields方法读取数据的顺序一致
 */
public class FlowBean implements Writable {

    // 电话号码，上行流量和下行流量
    private String phoneNum;
    private int upFlow;
    private int dFlow;
    private int amountFlow;

    // 构造器
    public FlowBean(String phoneNum, int upFlow, int dFlow) {
        this.phoneNum = phoneNum;
        this.upFlow = upFlow;
        this.dFlow = dFlow;
        this.amountFlow = upFlow + dFlow;
    }


    public FlowBean() {
    }


    // 输出流，序列化
    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeUTF(phoneNum);
        dataOutput.writeInt(upFlow);
        dataOutput.writeInt(dFlow);
        dataOutput.writeInt(amountFlow);
    }

    // 输入流，反系列化
    @Override
    public void readFields(DataInput dataInput) throws IOException {
        this.phoneNum = dataInput.readUTF();
        this.upFlow = dataInput.readInt();
        this.dFlow = dataInput.readInt();
        this.amountFlow = dataInput.readInt();
    }

    @Override
    public String toString() {

        return phoneNum + "," + upFlow + "," + dFlow + "," + amountFlow;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public int getUpFlow() {
        return upFlow;
    }

    public void setUpFlow(int upFlow) {
        this.upFlow = upFlow;
    }

    public int getdFlow() {
        return dFlow;
    }

    public void setdFlow(int dFlow) {
        this.dFlow = dFlow;
    }

    public int getAmountFlow() {
        return amountFlow;
    }

    public void setAmountFlow(int amountFlow) {
        this.amountFlow = amountFlow;
    }
}
