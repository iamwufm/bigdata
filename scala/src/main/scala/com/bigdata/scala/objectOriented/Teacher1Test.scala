package com.bigdata.scala.objectOriented

/**
 * Date:2023/10/31
 * Author:wfm
 * Desc:
 */
object Teacher1Test {
  def main(args: Array[String]): Unit = {
    // 创建对象
    val teacher = new Teacher1("张三",20)

    println(teacher.name)

    // 创建对象
    val teacher2 = new Teacher1("lisi",30,"男","广东省")

    println(teacher2.prov)

  }
}
