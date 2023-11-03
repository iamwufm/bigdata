package com.bigdata.scala.objectOriented

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc: 单例对象
 * 关键词：object
 * 定义的成员变量和方法都是静态的
 * 可以通过对象名.方法 或 对象名.成员变量
 */
object ScalaStatic {

  // 成员变量
  val name: String = "张三"
  var age: Int = 18

  // 成员方法
  def saySomething(msg: String) = {
    println(msg)
  }

  // ScalaStatic("油焖大虾") 默认就是调用apply方法
  // 语法糖（sugar）
  def apply(food: String) = {
    println(s"米饭一碗 $food")
  }
}
