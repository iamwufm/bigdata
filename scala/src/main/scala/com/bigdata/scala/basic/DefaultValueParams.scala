package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:默认参数值函数
 */
object DefaultValueParams {
  def main(args: Array[String]): Unit = {
    // 调用
    add()
    add(4)
    add(y = 10,x = 4)
  }

  def add(x: Int = 2, y: Int = 3): Int = {
    println(s"x + y = ${x+y}")
    x + y
  }

}
