package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:类的访问权限
 * 类的前面加上private:外部类或者外部对象不能访问（只能在这个类的内部以及其伴生类对象中可以访问修改）
 * 类的前面加上private[this]:标识这个类在当前包下都可见，当前包下的子包不可见
 * 类的前面加上private[包名]：标识这个类在当前包及其子包下都可见
 */
private[scala] class Teacher4(val name: String, var age: Int) {
  var gender: String = _
}
