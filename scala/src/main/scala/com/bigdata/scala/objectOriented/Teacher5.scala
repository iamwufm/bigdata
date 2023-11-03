package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:伴生类/apply 方法
 * 在 Scala 中, 当单例对象与某个类共享同一个名称时，他被称作是这个类的伴生对象。必须在同一个源文件里定义类和它的伴生对象
 */
class Teacher5(var name: String, var age: Int) {
}

// 在伴生对象中可以访问类的私有成员方法和属性
object Teacher5 {
  def apply(): Teacher5 = {
    // 初始化工作
    new Teacher5("张三", 20)
  }

  def main(args: Array[String]): Unit = {
    // object 类() 默认调用的 apply()方法
    // 不加()时不会调用
    // 等同于Teacher5.apply()
    val tea = Teacher5() //语法糖(sugar)
    println(tea.name)
  }

}
