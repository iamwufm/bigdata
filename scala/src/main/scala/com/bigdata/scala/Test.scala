package com.bigdata.scala

import com.bigdata.scala.objectOriented.{Teacher3, Teacher4, Teacher5}

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc:
 */
object Test {
  // 快捷键 main
  def main(args: Array[String]): Unit = {

    //    val teacher = new Teacher2("张三", 18,"男")
    //    println(teacher.name)

    val teacher = new Teacher3("李四", 20)
    //    println(teacher.name) // 报错，不能访问
    //    teacher.gender = "男"// 报错，不能访问
    //    println(teacher.gender)// 报错，不能访问
    val teacher1 = new Teacher4("李四", 20)

    val tea = Teacher5()
    println(tea.name)
  }
}
