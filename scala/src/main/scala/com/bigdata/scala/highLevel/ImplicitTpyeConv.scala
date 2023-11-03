package com.bigdata.scala.highLevel

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:隐式转换类型
 */
object ImplicitTpyeConv {

  implicit val fdouble2Int = (double: Double) => {
    println("---隐式函数---")
    double.toInt
  }

  implicit def double2Int(double: Double) = {

    println("---隐式方法---")
    double.toInt
  }

  def main(args: Array[String]): Unit = {
    println("-------------隐式类型转换---------")
    // age是一个Int类型，但是赋值的时候却是一个浮点型，此刻编译器会在当前上下文中找一个隐式转换，找一个能把浮点型变成Int的隐式转换
    // 优先隐式函数，再次是函数方法
    val age: Int = 20.5
    println(age)
    //    -------------隐式类型转换---------
    //    ---隐式函数---
    //    20
  }

}
