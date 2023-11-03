package com.bigdata.scala.highLevel

import java.io.File

import scala.io.Source

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:
 */
object ImplicitClassTest {

  // 隐式类-只能在静态对象中使用
  implicit class FileRead(file: File) {

    def read = Source.fromFile(file).mkString

  }
  def main(args: Array[String]): Unit = {
    val file = new File("C:\\Alearning\\data\\mr\\wc\\input\\test1.txt")

    println(s"FileContent = ${file.read}")

    // 导包
    import ImplicitClass._
    // 可以在单例对象中定义隐式方法或函数，然后导包就可以用了
    println("总行数", file.count())
    file.saySomeThing()

  }

}
