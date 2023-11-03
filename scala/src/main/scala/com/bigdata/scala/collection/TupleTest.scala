package com.bigdata.scala.collection

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:元组 -- 不可变
 */
object TupleTest {
  def main(args: Array[String]): Unit = {
    // 定义元组
    val t = (1,"hello",true)
    // 或者
    val t2 = new Tuple3(2,"hello",true)

    // 访问tuple中的元素
    println(t._2)

    // 迭代元组
    t.productIterator.foreach(println)

    // 对偶元组
    val t3 = (1,3)
    // 交换元组的元素位置，生成新的元组
    val swap: (Int, Int) = t3.swap

    println(swap) // (3,1)
  }

}
