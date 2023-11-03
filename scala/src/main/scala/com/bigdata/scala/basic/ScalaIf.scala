package com.bigdata.scala.basic

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc:if 条件表达式
 * if ... else if ... else 代码较多时可以使用代码块{}
 */
object ScalaIf {

  def main(args: Array[String]): Unit = {
    var age: Int = 18

    // 如果年龄是18就是一枝花
    var value = if (age == 18) "一枝花" else "不是一枝花"
    printf(value)

    var value2 = if (age == 18) {
      "一枝花"
      // 不需要写return，最后一行就是返回值
      "哈哈哈"
    }

    var value3 = if (age > 10) age

    var value4 = if (age < 18) "年龄小于18" else if (age == 18) "一枝花" else age

  }

}
