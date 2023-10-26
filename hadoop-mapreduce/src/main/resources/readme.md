一行数据的样子：

```txt
{"events":"1473367236143\\n","header":{"cid_sn":"1501004207EE98AA","mobile_data_type":"","os_ver":"22","mac":"1c:77:f6:78:f5:75","resolution":"1080x1920","commit_time":"1502686418952","sdk_ver":"103","device_id_type":"mac","city":"江门市","device_model":"HUAWEI VNS-AL00","android_id":"867830021735040","carrier":"中国xx","promotion_channel":"1","app_ver_name":"1.4","imei":"867830021735040","app_ver_code":"4010104","pid":"pid","net_type":"3","device_id":"m.1c:77:f6:78:f5:75","app_device_id":"m.1c:77:f6:78:f5:75","release_channel":"1009","country":"CN","time_zone":"28800000","os_name":"android","manufacture":"OPPO","commit_id":"fde7ee2e48494b24bf3599771d7c2a78","account":"none","app_token":"XIAONIU_A","app_id":"com.appid.xiaoniu","language":"zh","build_num":"YVF6R16303000403"}}
```

==要求如下：==

（1）必选字段不能为空（null或去掉空格后是空字符串）。必须字段见[[#4.1 建外部表映射预处理数据]] net_type 字段（含）以上都是必选字段

（2）为每条日志添加一个用户唯一标识字段：user_id

规则：如果是os_name="android" and android_id不为空， user_id = android_id，否则为 user_id = device_id

（3）将event字段抛弃，将header中的各字段解析成普通文本行

（4）需要将清洗后的结果数据，分ios和android两种类别，输出到两个不同的文件夹

==代码实现==：com.bigdata.log.mr.AppLogDataCleanMain

==运行==：

```shell
# hadoop jar 包名.jar  参数1 参数2(在pom文件指定了主类。可以不用写类名))
hadoop jar appLogDataClean-jar-with-dependencies.jar /app-log-data/data/20170814 /app-log-data/clean/20170814
```

