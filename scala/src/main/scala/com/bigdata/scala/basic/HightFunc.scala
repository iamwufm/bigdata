package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:高阶函数: 参数是函数 或者 返回值是函数
 */
object HightFunc {
  def main(args: Array[String]): Unit = {
    def f1(f: Int => String, v: Int) = f(v)

    def f2(x: Int) = "[" + x.toString + "]"

    // 调用方法
    println(f1(f2, 4))

    // 传递的函数有两个参数
    def add(x: Int, y: Int, op: (Int, Int) => Int) = op(x, y)

    val f3 = (x: Int, y: Int) => x + y
    // 标准版
    add(2, 3, f3)
    add(2, 3, (x: Int, y: Int) => x + y)
    // 参数的类型可以省略
    add(2, 3, (x, y) => x + y)
    // 如果参数只出现一次，则参数省略且后面参数可以用_代替
    add(2, 3, _ + _)

  }

}
