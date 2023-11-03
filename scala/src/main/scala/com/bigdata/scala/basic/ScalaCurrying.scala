package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:柯里化(Currying)
 * 柯里化(Currying)指的是将原来接受两个参数的函数变成新的接受一个参数的函数的过程。新的函数返回一个以原有第二个参数为参数的函数。
 */
object ScalaCurrying {

  def main(args: Array[String]): Unit = {
    def add(x: Int, y: Int) = x + y

    // 把add变成柯里化。经过柯里化之后，函数的通用性有所降低，但是适应性有所提高
    def add1(x: Int)(y: Int) = x + y

    println(add1(2)(3))

    // 分析下其演变过程
    def add3(x: Int) = (y: Int) => x + y

    // (y: Int) => x + y 为一个匿名函数, 也就意味着 add 方法的返回值为一个匿名函数

    val result = add3(5) // Int => Int = $Lambda$1076/1792476109@2c59b9f4

    println(result(3))
    println(result(8))
  }

}
