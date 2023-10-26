package com.bigdata.hbase;

import java.io.UnsupportedEncodingException;

/**
 * Date:2023/10/19
 * Author:wfm
 * Desc:
 * 可用过python查看
 * str 类型和 bytes 类型之间就需要使用 encode() 和 decode() 方法进行转换。
 * encode()编码 ： str 类型 -> bytes 类型
 * decode()解码：bytes 类型 -> str 类型
 */
public class Test {
    public static void main(String[] args) throws UnsupportedEncodingException {

        byte[] bytes = "0".getBytes();

        System.out.println("001".hashCode());

    }


}
