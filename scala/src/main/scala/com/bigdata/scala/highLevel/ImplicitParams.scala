package com.bigdata.scala.highLevel

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:隐式参数
 * 定义方法时，可以把参数列表标记为 implicit，表示该参数是隐式参数
 *
 * 当调用包含隐式参数的方法时，如果当前上下文中有合适的隐式值，则编译器会自动为该
 * 组参数填充合适的值，且上下文中只能有一个符合预期的隐式值（其实是根据上面内容）。如果没有编译器会抛出
 * 异常。当然，标记为隐式参数的我们也可以手动为该参数添加默认值。
 */
object ImplicitParams {

  // // 编译器在查找隐式值的时候，不能有歧义
  implicit val str = "hello,baby"
  //    implicit val str2 = "明晚十点见" 有两个相同数据类型的变量，say 执行会报错，不知道要用哪一个变量
  //    定义方法时，可以把参数列表标记为 implicit，表示该参数是隐式参数
  def say(implicit content: String) = println(content)

  def say2(implicit age: Int = 28) = println(age)

  // 方法的参数如果有多个隐式参数的话，只需要使用一个implicit关键字即可
  // 隐式参数列表必须放在方法的参数列表后面
  def addPlus(a: Int)(implicit b: Int, c: Int) = a + b + c

  def main(args: Array[String]): Unit = {

    say // hello,baby
    say2 // 28 优先用输入的值，再次时隐式变量值，最后时默认值

    implicit val num = 3
    println(addPlus(2)) // 8
  }
}
