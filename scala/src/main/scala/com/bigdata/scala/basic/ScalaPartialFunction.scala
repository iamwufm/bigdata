package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:偏函数：PartialFunction[参数类型，返回值类型]
 */
object ScalaPartialFunction {

  def main(args: Array[String]): Unit = {

    def func(str: String): Int = {
      if (str.equals("a")) 97
      else 0
    }

    // 偏函数 PartialFunction[String, Int] 中String为输入参数数据类型，Int为返回数据类型
    def func1: PartialFunction[String, Int] = {
      case "a" => 97
      case _ => 0
    }

    // 偏函数
    def func3: PartialFunction[Any, Any] = {
      case i: Int => i * 10
      case i:String => i
      case _ => "hahaha"
    }

    println(func("a"))
    println(func1("a"))
    println(func1("b"))

    println(func3("hello"))
    println(func3(30))
    println(func3(32.1))
  }

}
