package com.bigdata.scala.basic

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc:方法的定义与调用
 *
 * 格式：def methodName ([list of parameters]) [: return type] = {}
 * 方法的返回值类型可以不写，编译器可以自动推断出来，但是对于递归函数，必须指定返回
 */
object ScalaMethod {
  def main(args: Array[String]): Unit = {
    // 定义方法
    def sum(num1: Int, num2: Int) = num1 + num2

    // 调用
    val result = sum(1, 4)

    println(result)

    def printHell1() = println("hello scala")
    def printHello2 = println("hello spark")

    // 调用
    printHell1 // 没有参数输入，可以 printHell1 或 printHell1() 调用
    printHell1()
    printHello2 // 这个只能这样调用

  }

}
