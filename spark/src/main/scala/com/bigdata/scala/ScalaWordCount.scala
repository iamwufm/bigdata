package com.bigdata.scala

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
 * Date:2023/10/27
 * Author:wfm
 * Desc:单词统计(spark和scala的依赖版本号要跟集群中的一致${SPARK_HOME}/jars/scala-library-2.12.18.jar
 *                                                                    /spark-core_2.12-3.5.0.jar)
 * 写好程序，打包上传集群运行
spark-submit --master spark://hadoop101:7077 --class com.bigdata.scala.ScalaWordCount original-spark-1.0-SNAPSHOT.jar hdfs://hadoop101:8020/input hdfs://hadoop101:8020/outputscala
 * 或者 本地模式运行spark程序，setMaster("local[*]")
 */
object ScalaWordCount {
  // 快捷键 main
  def main(args: Array[String]): Unit = {
    // 1.创建spark执行的入口
//    val conf: SparkConf = new SparkConf().setAppName("ScalaWordCount")
    // 本地运行
    val conf: SparkConf = new SparkConf().setAppName("ScalaWordCount").setMaster("local[*]")
    val sc: SparkContext = new SparkContext(conf)

    // 2.逻辑处理
    // 获取数据源。 存的是一行数据
    // C:\Alearning\data\mr\wc\input
    // hdfs://hadoop101:8020/input
    val lines: RDD[String] = sc.textFile(args(0))
    // 切分压平：按空格切分
    val words: RDD[String] = lines.flatMap(_.split(" "))
    // 将单词和一组合
    val wordAndOne: RDD[(String, Int)] = words.map((_, 1))
    // 将key进行聚合
    val reduce: RDD[(String, Int)] = wordAndOne.reduceByKey(_ + _)
    // 排序
    val sorted: RDD[(String, Int)] = reduce.sortBy(_._2, false)
    // 将数据保存在指定目录
    // C:\Alearning\data\mr\wc\outputscala
    // hdfs://hadoop101:8020/outputscala
    sorted.saveAsTextFile(args(1))

    // 3.释放资源
    sc.stop()


  }

}
