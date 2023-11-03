package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:数组的定义
 *
 * 长度不可变，内容可改
 */
object ArrayOpt {
  def main(args: Array[String]): Unit = {

    var arr = new Array[String](3)
    arr(0) = "hello" // 修改内容
    println(arr.toBuffer) // ArrayBuffer(null, null, null)

    var arr1 = Array("hello", "scala") // ArrayBuffer(hello, null, null)
    arr1(0) = "Python"
    println(arr1.toBuffer) // ArrayBuffer(Python, scala)


    println("=====================单词统计========================")
    val lines: Array[String] = Array("hello hello tom", "hello jerry")

    // 1.每个元素按空格切分 Array(Array(hello, hello, tom), Array(hello, jerry))
    val wordsArr: Array[Array[String]] = lines.map((x: String) => x.split(" "))
    // 可以简化为
    //    lines.map(x => x.split(" "))
//    lines.map(_.split(" "))

    // 2. 把每个元素扁平化 Array[String] = Array(hello, hello, tom, hello, jerry)
    val words: Array[String] = wordsArr.flatten

    // 1和2可以写成
    //    val words: Array[String] = lines.flatMap(_.split(" "))

    // 3.把相同的元素聚合在一起 scala.collection.immutable.Map[String,Array[String]] =
    // Map(hello -> Array(hello, hello, hello), tom -> Array(tom), jerry -> Array(jerry))
    val groupBy: Map[String, Array[String]] = words.groupBy(x => x)

    // 4.统计map的value值的长度 scala.collection.immutable.Map[String,Int] = Map(hello -> 3, tom -> 1, jerry -> 1)
    val wordCount: Map[String, Int] = groupBy.mapValues(_.length)

    // 5.排序。按value值升序
    val result: List[(String, Int)] = wordCount.toList.sortBy(_._2)

    // 遍历结果
    result.foreach(println(_))

  }

}
