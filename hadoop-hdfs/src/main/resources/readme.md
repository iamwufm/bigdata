在window上安装hadoop客户端。具体操作参见[[window安装hadoop]]

（1）增删查改操作：com.bigdata.hdfs.HdfsClientTestMian

---

（2）把日志上传到hdfs：com.bigdata.datacollect.DataCollectMain

模拟产生日志的代码在web-log模块下的com.bigdata.CollectLogMain

```txt
在业务系统的服务器上，业务程序会不断生成业务日志（比如网站的页面访问日志）
业务日志使用log4j生成的，会不断地切出日志文件

需求：
需要定期（比如每小时）从业务服务器上地日志目录中，探测需要采集地日志文件（access.log不能采），发往HDFS
当天采集到日志要放在hdfs地当天目录中
采集完成地日志文件，需要移动到日志服务器一个备份目录中
定期检查（比如每小时）备份目录，将备份时长超过24小时地日志文件清除

注意点：业务服务器可能有多台（hdfs上地文件名不能直接用日志服务器上地文件名）
```
---
（3）统计单词次数：com.bigdata.wordcount.HdfsWordcountMain

```txt
单词统计

统计某个目录下的所有文件内容的单词出现次数
文件内容以空格切分

把统计结果放在hdfs某目录下
```

---

高可用：com.bigdata.hdfs.HAHdfsClientMian
