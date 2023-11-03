package com.bigdata.scala.objectOriented

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc: 主构造器和辅助构造器
 * 定义在类名称后面的构造器叫主构造器，一个类可以有多个辅助构造器（在辅助构造器中必须先调用类的主构造器）
 * 类的主构造器中的属性会定义成类的成员变量
 * 如果主构造器中成员属性没有val | var 修饰的话，该属性不能被访问，相当于对外没有提供get方法
 */
class Teacher1(var name: String, age: Int) {
  var sex: String = _
  var prov: String = _

  // 定义辅助构造器
  def this(name: String, age: Int, sex: String) = {
    this(name, age) // 在辅助构造器中必须先掉主构造器
    this.sex = sex
  }

  def this(name: String, age: Int, sex: String, prov: String) = {
    this(name, age, sex) // 在上面一个辅助构造器中调用了主构造器
    this.prov = prov
  }
}
