package com.bigdata.scala.collection

import scala.collection.parallel.immutable.ParSeq

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc:为了充分使用多核CPU，提供了并行集合。调用集合的 par 方法, 会将集合转换成并行化集合。
 */
object ParTest {
  def main(args: Array[String]): Unit = {
    // 不开启并行化操作
    val result1 = (0 to 100).map(
      case_ => Thread.currentThread().getName
    ).distinct

    // 开启并行化操作
    val result2 = (0 to 100).par.map(
      case_ => Thread.currentThread().getName
    ).distinct

    println(result1) // Vector(main)
    println(result2) // ParVector(scala-execution-context-global-20, scala-execution-context-global-30, 。。。)

  }
}
