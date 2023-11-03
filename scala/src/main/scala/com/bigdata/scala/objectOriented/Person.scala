package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:动态的混入N个特质
 */
object Person {
  def main(args: Array[String]): Unit = {
    // 在创建对象的时候，动态混入N个特质
    val stu = new Student() with FlyTrait with ScalaTrait {
      override var name: String = "张三"

      override def hello(name: String): Unit = {
        println(s"hello,${name}")
      }
    }

    stu.hello(stu.name)
  }

}
class Student {}