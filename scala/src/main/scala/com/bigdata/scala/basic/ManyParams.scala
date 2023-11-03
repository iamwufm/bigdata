package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc: 可变参数函数
 */
object ManyParams {
  def main(args: Array[String]): Unit = {
    // 方法的调用
    methodManyParams("hello","scala","hello python")
  }

  def methodManyParams(a: String*): Unit ={
    for (elem <- a) println(elem)
  }
}
