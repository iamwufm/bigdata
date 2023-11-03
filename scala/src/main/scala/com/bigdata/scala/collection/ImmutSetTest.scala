package com.bigdata.scala.collection



/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:不可变的Set—HashSet
 */
object ImmutSetTest {
  def main(args: Array[String]): Unit = {
    // 导包
    import scala.collection.immutable.HashSet
    // 创建一个不可变的HHashSet
    val set1 = new HashSet[Int]()

    // 将元素和set1合并生成一个新的set
    val set2 = set1 + 4
    // set中的元素不能重复
    val set3 = set1 ++ Set(5,6,7) ++ Set(5,6,1)
    println(set3)

  }
}
