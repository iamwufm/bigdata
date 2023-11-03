package com.bigdata.scala.collection

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc:在 Scala 中 Option 类型样例类用来表示可能存在或也可能不存在的值(Option 的子类有 Some和 None)。
 * Some 包装了某个值，None 表示没有值。
 */
object OptionTest {
  def main(args: Array[String]): Unit = {
    // 创建map集合
    val mp = Map("a" -> 1, "b" -> 2, "c" -> 3)

    // Map 的 get 方法返回的为 Option, 也就意味着 rv 可能取到也有可能没取到
    val value: Option[Int] = mp.get("b")

    // 如果 value=None 时，会出现异常情况
    println(value.get)

  }

}
