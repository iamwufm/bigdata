package com.bigdata.scala.basic

/**
 * Date:2023/10/28
 * Author:wfm
 * Desc:字符串的格式化输出
 * 三种形式
 *
 */
object ScalaPrint {
  def main(args: Array[String]): Unit = {
    val name = "巧克力"
    val price = 2880.135
    val url = "http://xx/sss"

    println("--------1. 普通打印---------------")
    println("产品名称：" + name, "价格：" + price)

    println("--------2.文字'f'插值器允许创建一个格式化的字符串，类似于 C 语言中的 printf。---------------")
    // 在使用'f'插值器时，所有变量引用都应该是 printf 样式格式说明符，如％d，％i，％f 等。
    println(f"产品名称：$name%s，价格：$price%1.2f，网址是$url ")
    println(f"产品名称：$name，价格：$price，网址是$url ")
    printf("产品名称：%s，价格：%1.2f，网址是%s ", name, price, url) // 不会换行
    println()

    println("--------3.'s'允许在处理字符串时直接使用变量---------------")
    // 字符串插入器还可以处理任意表达式。
    println(s"产品名称：$name，价格：$price，网址是$url")
    println(s"1+1=${1 + 1}")

    val stu = new Student("taotao", 18)
    println(s"${stu.name}")

    println("--------多行字符串，在 Scala中，利用三个双引号包围多行字符串就可以实现---------------")
    val str =
      """
        |select
        |name,age
        |from user;
        """.stripMargin

    println(str)

  }
}

class Student(val name: String, var age: Int)
