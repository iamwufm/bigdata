package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:使用关键字abstract 定义一个抽象类
 * 可以定义属性、未实现的方法和具体实现的方法
 */
abstract class AbsClass {

  def eat(food: String): String

  def swimming(style: String) = {
    println(s"$style 这么游")
  }

}
