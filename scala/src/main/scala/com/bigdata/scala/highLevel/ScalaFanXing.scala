package com.bigdata.scala.highLevel

import com.bigdata.scala.highLevel.ClothesEnum.ClothesEnum

/**
 * Date:2023/11/3
 * Author:wfm
 * Desc:泛型
 * 就是类型约束
 * 这个类型可以代表任意的数据类型，一般用T表示
 */
// 约束content变量的类型为泛型
class Message[T](content: T)

// 定义一个泛型类衣服
class Clothes[A, B, C](val clothType: A, val color: B, val size: C)

// 枚举类
object ClothesEnum extends Enumeration {
  type ClothesEnum = Value
  val 上衣, 内衣, 裤子 = Value

}

object ScalaFanXing {

  def main(args: Array[String]): Unit = {
    val message1: Message[String] = new Message[String]("hello world")
    val message2: Message[Int] = new Message(18)

    val clth1 = new Clothes[ClothesEnum, String, Int](ClothesEnum.上衣, "black", 150)
    println(clth1.clothType)

    val clth2 = new Clothes(ClothesEnum.上衣, "black", "M")
    println(clth2.size)

  }
}

