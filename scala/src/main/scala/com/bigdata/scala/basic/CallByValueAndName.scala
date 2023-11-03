package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:传值调用与传名调用
 * 传值调用：在值作为参数传递给方法
 * 传名调用：把方法名称（函数）作为参数传递给方法
 */
object CallByValueAndName {
  // 钱包总金额
  var money = 50;

  // 每次扣5块钱
  def huaQian(): Unit = {
    money = money - 5
  }

  // 数钱，看看卡里还有多少
  def shuQian(): Int = {
    huaQian()
    money
  }

  // 传值
  def printByValue(x: Int) = {
    for (a <- 0 until 3) println(s"每次都算算还剩：${x}元")
  }

  // 传名
  // => 是一个函数的标志。需要一个没有参数，返回值为Int类型的函数
  def printByName(x: => Int) = {
    for (a <- 0 until 3) println(s"每次都算算还剩：${x}元")
  }

  def main(args: Array[String]): Unit = {
    // 传值调用
    // 1.计算shuQian的返回值=45
    // 2.将45作为参数参数传入printByValue
    printByValue(shuQian)

    // 传名调用
    // 将shuQian方法名称（会自动转为函数）传递到方法的内部执行
    printByName(shuQian)
  }

}
