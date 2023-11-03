package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:类的成员属性访问权限
 * 如果类成员属性是private修饰的，它的set和get方法都是私有的，外部不能访问（只能在这个类的内部以及其伴生类对象中可以访问修改）
 */
class Teacher3(private val name: String, var age: Int) {
  private var gender: String = _
}

object Teacher3{
  def main(args: Array[String]): Unit = {
    val teacher = new Teacher3("李四",20)
    println(teacher.name)
    teacher.gender = "男"
    println(teacher.gender)
  }
}

