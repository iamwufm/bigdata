package com.bigdata.log.mr;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.output.MultipleOutputs;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date:2023/10/16
 * Author:wfm
 * Desc: reduce的map阶段
 * 输入：一行的偏移量，一行数据
 * 输出：Text, NullWritable
 */
public class AppLogDataCleanMapper extends Mapper<LongWritable, Text, Text, NullWritable> {

    Text k = null;
    NullWritable v = null;
    SimpleDateFormat sdf = null;
    MultipleOutputs<Text, NullWritable> mos = null;  //多路输出器

    @Override
    protected void setup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
            throws IOException, InterruptedException {
        k = new Text();
        v = NullWritable.get();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        mos = new MultipleOutputs<Text, NullWritable>(context);
    }


    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        // {"events":"1473367236143\n","header":{"cid_sn":"1501004207EE98AA",...,"language":"zh","build_num":"YVF6R16303000403"}}
        // 获取json
        JSONObject jsonObject = JSON.parseObject(value.toString());
        // 获取header里面的json串
        JSONObject header = jsonObject.getJSONObject("header");

        // 过滤缺失必选字段，如下某一值为空都放弃这行数据
        // 相当于null == headerObj.getString("sdk_ver") || "".equals(headerObj.getString("sdk_ver").trim())
        if (StringUtils.isBlank(header.getString("sdk_ver"))) {
            return;
        }

        if (StringUtils.isBlank(header.getString("time_zone"))) {
            return;
        }

        if (StringUtils.isBlank(header.getString("commit_id"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("commit_time"))) {
            return;
        } else {
            // 练习时追加的逻辑，替换掉原始数据中的时间戳
            String commit_time = header.getString("commit_time");
            String format = sdf.format(new Date(Long.parseLong(commit_time)));
            header.put("commit_time", format);
        }
        if (StringUtils.isBlank(header.getString("pid"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("app_token"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("app_id"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("device_id"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("device_id_type"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("release_channel"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("app_ver_name"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("app_ver_code"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("os_name"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("os_ver"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("language"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("country"))) {
            return;
        }

        if (StringUtils.isBlank(header.getString("manufacture"))) {
            return;
        }

        if (StringUtils.isBlank(header.getString("device_model"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("resolution"))) {
            return;
        }
        if (StringUtils.isBlank(header.getString("net_type"))) {
            return;
        }

        // 生成user_id
        // 如果os_name的值是android且android_id的值不为空，则为android_id的值，否则为device_id的值
        String user_id = "";
        if ("android".equals(header.getString("os_name").trim())) {
            user_id = StringUtils.isNotBlank(header.getString("android_id")) ? header.getString("android_id")
                    : header.getString("device_id");
        } else {
            user_id = header.getString("device_id");
        }


        // 输出结果
        header.put("user_id", user_id);
        k.set(JsonToStringUtil.toString(header));

        // 需要将清洗后的结果数据，分ios和android两种类别，输出到2个不同的文件夹
        if ("android".equals(header.getString("os_name"))) {
            mos.write(k, v, "android/android");
        } else {
            mos.write(k, v, "ios/ios");
        }

    }

    @Override
    protected void cleanup(Mapper<LongWritable, Text, Text, NullWritable>.Context context)
            throws IOException, InterruptedException {
        mos.close();
    }
}
