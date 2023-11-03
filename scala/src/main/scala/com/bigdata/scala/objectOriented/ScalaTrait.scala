package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:特质（interface）
 * 在Scala中特质中可以定义有实现的方法，也可以定义没有实现的方法
 */
trait ScalaTrait {

  // 成员变量
  var name: String
  var age: Int = 18

  // 没有实现的方法
  def hello(name: String)

  // 实现的方法
  def small(name: String) = {
    println(s"老赵对 $name 妩媚一笑")
  }
}
