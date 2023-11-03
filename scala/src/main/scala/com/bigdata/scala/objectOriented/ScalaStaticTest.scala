package com.bigdata.scala.objectOriented

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc:
 */
object ScalaStaticTest {

  def main(args: Array[String]): Unit = {
    // 查看成员变量
    println(ScalaStatic.name)
    ScalaStatic.age = 20
    println(ScalaStatic.age)

    // 查看成员方法
    ScalaStatic.saySomething("今天有点冷")
    ScalaStatic.apply("西红柿炒番茄")

    // 默认调用就是apply方法
    ScalaStatic("油焖大虾") // 语法糖（suger）

  }

}
