package com.bigdata.scala.highLevel

import java.io.{BufferedReader, File, FileReader}

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:
 */
object ImplicitClass {
  //  定义了一个隐式方法，将file类型变成RichFile
  implicit def file2RichFile(file: File) = new RichFile(file)

  // 隐式将Student -> Ordered[Student]
  implicit def student2OrderedStu(stu: Students2) = new Ordered[Students2]{
    override def compare(that: Students2): Int = stu.age - that.age
  }

  // 一个隐式对象实例 -> 一个又具体实现的Comparator
  implicit val comparatorStu = new Ordering[Students2] {
    override def compare(x: Students2, y: Students2): Int = x.age - y.age
  }
}


class RichFile(file: File) {
  /**
   * 返回文件的记录行数
   *
   * @return
   */
  def count(): Int = {
    val fileReader = new FileReader(file)
    val bufferedReader = new BufferedReader(fileReader)
    var sum = 0
    try {
      var line = bufferedReader.readLine()
      while (line != null) {
        sum += 1
        line = bufferedReader.readLine()
      }
    } catch {
      case _: Exception => sum
    } finally {
      fileReader.close()
      bufferedReader.close()
    }
    sum
  }

  def saySomeThing() = {
    println("稍等下，有话要对你说。。。")
  }

}