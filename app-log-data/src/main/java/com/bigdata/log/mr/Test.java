package com.bigdata.log.mr;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date:2023/10/16
 * Author:wfm
 * Desc:
 */
public class Test {
    public static void main(String[] args) {
        String str = "{\"cid_sn\":\"1501004207EE98AA\",\"mobile_data_type\":\"\",\"os_ver\":\"22\",\"mac\":\"1c:77:f6:78:f5:75\"}";
        JSONObject jsonObject = JSON.parseObject(str);

        System.out.println(jsonObject.get("cid_sn"));

        String str1 = " ";
        System.out.println(StringUtils.isBlank(str1));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String format = sdf.format(new Date(Long.parseLong("1502686418952")));
        System.out.println(format.replace("2017-08","2023-10"));
    }
}
