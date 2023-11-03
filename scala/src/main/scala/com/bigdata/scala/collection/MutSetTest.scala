package com.bigdata.scala.collection

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:可变的Set—HashSet
 */
object MutSetTest {
  def main(args: Array[String]): Unit = {
    // 导包
    import scala.collection.mutable.HashSet
    // 创建一个可变的HashSet
    val set1 = new HashSet[Int]()
    // 向set1中添加元素
    set1 += 2
    set1.add(4)
    set1 ++= Set(1,3,5)
    println(set1)

    // 删除一个元素
    set1 -= 5
    set1.remove(2)
    println(set1)

  }
}
