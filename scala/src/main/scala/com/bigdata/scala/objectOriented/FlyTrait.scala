package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:特质（interface）
 * 在Scala中特质中可以定义有实现的方法，也可以定义没有实现的方法
 *
 * 被 final 修饰的类不能被继承；
 * 被 final 修饰的属性不能重写；
 * 被 final 修饰的方法不能被重写
 */
trait FlyTrait {
  final val animalName: String = "bird"

  final def fly(name: String) = {
    println(s"看，${name}在飞")
  }

}
