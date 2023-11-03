package com.bigdata.scala.basic

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc:函数的定义与调用
 * 格式：
 * (函数参数列表) => 函数体
 *
 * 函数名称:(参数类型) =>函数返回值=(参数引用)=>函数体
 */
object ScalaFunction {
  def main(args: Array[String]): Unit = {
    // 定义函数
    val f1 = (x: Int) => x * 10

    //调用
    println(f1(5))

    // 匿名函数
    (x: Int, y: Int) => x * y

      // 定义函数
      val f2: (Int, Int) => Int = (x, y) => x * y

      // 调用
      println(f2(3, 5))

  }

}
