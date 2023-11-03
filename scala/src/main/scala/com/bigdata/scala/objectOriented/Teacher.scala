package com.bigdata.scala.objectOriented

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc: 定义类
 * 如果成员属性使用var修饰的话，相当于对外提供了get和set方法
 * 如果成员属性使用了val修饰的话，相当于对外提供了get方法
 */
class Teacher {

  // _ 表示一个占位符, 编译器会根据你变量的具体类型赋予相应初始值
  // 注意: 使用_ 占位符是, 变量类型必须指定
  var name: String = _
  val age: Int = 18 // 不可修改

  def sayHello() = {
    println(s"hello,$name")
  }
}
