package com.bigdata.scala.collection

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:不可变的Map—HashMap
 */
object ImmutMapTest {
  def main(args: Array[String]): Unit = {
    // 导包
    import scala.collection.immutable.HashMap
    // 定义一个不可变的HashMap
    val map1 = HashMap(("hadoop", 2), ("hello", 4))

    // 遍历元素
    for (elem <- map1.keySet) {
      println(elem, "->", map1(elem))
    }

    map1.foreach{case(x,y) => println(x+"->"+y)}

    for (elem <- map1) {println(elem._1,elem._2)}

  }

}
