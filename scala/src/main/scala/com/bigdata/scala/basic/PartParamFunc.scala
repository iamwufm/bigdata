package com.bigdata.scala.basic

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:部分参数应用函数
 * 如果只传递几个参数并不是全部参数，那么将返回部分应用的函数。这样就可以方便地绑定一些参数，其余的参数可稍后填写补上。
 */
object PartParamFunc {
  def main(args: Array[String]): Unit = {

    def add(x: Int, y: Int) = x + y

    // 类似 val add1 = (x:Int)=> add(2,x)
    val add1 = add(2,_:Int) // add1: Int => Int = $Lambda$1036/695660374@4345fd45

    println(add1(4))
  }

}
