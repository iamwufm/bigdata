package com.bigdata.scala.basic

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc: 变量的定义
 * 格式：var|val 变量名称 (: 数据类型) = 变量值
 *
 * - 定义变量使用 var 或者 val 关键字
 * - 使用 val 修饰的变量, 值不能为修改, 相当于 java 中 final 修饰的变量
 * - 使用 var 修饰的变量, 值可以修改
 * - 定义变量时, 可以指定数据类型, 也可以不指定, 不指定时编译器会自动推测变量的数据类型
 */
object ScalaVar {
  def main(args: Array[String]): Unit = {

    var name = "zhangsan"
    name = "lisi"

    val name2 = "zhaosi"
    //    name2 = "wangwu"

    var age: Int = 18

    println(name)

  }
}
