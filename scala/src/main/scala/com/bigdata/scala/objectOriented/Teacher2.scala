package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:构造器的访问权限
 * private 在主构造器之前，这说明该类的主构造器是私有的，外部类或者外部对象不能访问(只能在这个类的内部以及其
 * 伴生类对象中可以访问修改)
 * 也适用于辅助构造器
 */
class Teacher2 private(val name: String, var age: Int) {
  var gender: String = _

  // 辅助构造器，使用def this
  // 在辅助构造器中必须先调用类的主构造器
  def this(name: String, age: Int, gender: String) {
    this(name, age)
    this.gender = gender
  }
}

object Teacher2 {
  def main(args: Array[String]): Unit = {
    val teacher = new Teacher2("张三", 18)
    println(teacher.name)
  }
}
