package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:特质（interface）
 * 在Scala中特质中可以定义有实现的方法，也可以定义没有实现的方法
 *
 * 可以通过 type 关键字来声明类型。
 * // 把 String 类型用 S 代替
 * type S = String
 * val name: S = "小星星"
 * println(name)
 */
trait StudentTrait {

  type T

  def learn(s: T) = {
    println(s)
  }

}
