package com.bigdata.scala.basic

import scala.collection.mutable.ArrayBuffer

/**
 * Date:2023/11/3
 * Author:wfm
 * Desc:模拟 Map 映射、filter 过滤、Reduce 聚合
 */
object HightFuncDemo {
  def main(args: Array[String]): Unit = {
    val arr = Array(1, 2, 3, 4)

    println("-------------------map 映射------------------------")

    def map(arr: Array[Int], op: Int => Int) = {
      for (elem <- arr) yield op(elem)
    }
    // arr.map((x: Int) => x * 10)
    map(arr, (x: Int) => x * 10)
    val arr1 = map(arr, _ * 10)
    println(arr1.mkString(","))

    println("-------------------filter 过滤------------------------")

    //    arr.filter()
    def filter(arr: Array[Int], op: Int => Boolean) = {
      val arrBuffer = ArrayBuffer[Int]()
      for (elem <- arr if op(elem)) {
        arrBuffer += elem
      }
      arrBuffer.toArray
    }

    // arr.filter(_ > 2)
    filter(arr, (x: Int) => x > 2)
    val arr2 = filter(arr, _ > 2)
    println(arr2.mkString(","))

    println("-------------------reduce 聚合------------------------")

    def reduce(arr: Array[Int], op: (Int, Int) => Int) = {
      var total = arr(0)
      for (elem <- 1 until arr.length) {
        total = op(total, arr(elem))
      }
      total
    }

    //    arr.reduce(_ + _)
    reduce(arr, (x, y) => x + y)
    val result = reduce(arr, _ + _)
    println(result)


  }

}
