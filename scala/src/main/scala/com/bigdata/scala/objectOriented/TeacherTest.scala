package com.bigdata.scala.objectOriented

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc:
 */
object TeacherTest {
  def main(args: Array[String]): Unit = {
    // 创建对象，也可以写成 new Teacher()
    val teacher = new Teacher
    teacher.name = "张三"

    teacher.sayHello()
  }

}
