package com.bigdata.scala.collection

import scala.collection.mutable

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:可变的Map—HashMap
 */
object MutMapTest {
  def main(args: Array[String]): Unit = {
    import scala.collection.mutable.HashMap
    // 创建一个可变的HashMap
    val map1 = new HashMap[String,Int]()

    // 向map中添加数据
    map1("spark") = 1
    map1 += (("hadoop",2))
    map1.put("storm",3)

    println(map1) // Map(hadoop -> 2, spark -> 1, storm -> 3)

    // 取值
    map1.get("spark")
    map1.getOrElse("python",5)

    // 从map1中移除元素
    map1 -= "spark"
    map1.remove("hadoop")
    println(map1) // Map(storm -> 3)

  }

}
