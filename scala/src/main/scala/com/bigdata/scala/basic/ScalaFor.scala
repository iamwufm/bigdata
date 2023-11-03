package com.bigdata.scala.basic

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc:循环语句/yeil关键字
 * 在 scala 中有 for 循环和 while 循环，用 for 循环比较多
 * for 循环语法结构：for (i <- 表达式/数组/集合)
 */
object ScalaFor {
  def main(args: Array[String]): Unit = {
    // 定义一个数组
    val array = Array(1, 2, 3, 4, 5)

    for (elem <- array) println(elem)
    for (ele <- 0 to 4) println(ele) // 0 to 5 => 会生成一个范围集合 Range(0,1,2,3,4)
    for (i <- 0 until 4) println(array(i)) // 0 until 4 => 会生成一个范围集合 Range(0,1,2,3)

    // 嵌套循环
    for (i <- 1 to 3; j <- 1 to 3 if i != j) {
      println(s"10 * $i + $j =" + (10 * i + j) + "\t")
    }

    // 把结果放入一个新的集合中
    // 选取偶数
    val array2 = for (e <- array if e % 2 == 0) yield e

    // 循环步长
    for (i <- 1 to 5 by 2) println(i) // 1,3,5

  }

}
