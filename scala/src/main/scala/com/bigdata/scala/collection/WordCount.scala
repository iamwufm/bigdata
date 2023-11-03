package com.bigdata.scala.collection

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc:单词统计
 */
object WordCount {
  def main(args: Array[String]): Unit = {
    val words = Array("hello tom hello star hello sheep", "hello tao hello tom")

    // 方法一
    val result = words.flatMap(_.split(" ")) // 按空格切分，然后压平
      .groupBy(x => x) // 分组
      .mapValues(_.length) // 取value值算长度
      .toList // 转集合

    println(result) // List((tao,1), (sheep,1), (star,1), (tom,2), (hello,5))

    // 方法二
    val result2 = words.flatMap(_.split(" "))
      .groupBy(x => x)
      .map(x => (x._1,x._2.length))
      .toList

    println(result2) // List((tao,1), (sheep,1), (star,1), (tom,2), (hello,5))

  }

}
