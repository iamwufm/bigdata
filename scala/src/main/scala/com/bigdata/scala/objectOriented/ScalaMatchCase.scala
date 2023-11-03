package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:模式匹配 match case
 * 一旦一个case 匹配上了，就不会再往下匹配了
 */
object ScalaMatchCase {
  def main(args: Array[String]): Unit = {

    println("---------------------匹配字符串内容-------------------------")

    def contentMatch(str: String) = str match {
      case "hello" => println("hello")
      case "Dog" => println("DOg")
      case "1" => println("1")
      case "1" => println("2")
      case _ => println("匹配不上") // _ 任意内容
    }

    contentMatch("hello")
    contentMatch("Dog")
    contentMatch("1")
    contentMatch("you")

    println("---------------------匹配数据类型-------------------------")

    def typeMatch(x: Any) = x match {
      case x: Int => println(s"Int $x")
      case y: Long => println(s"Long $y")
      case b: Boolean => println(s"boolean $b")
      case _ => println("匹配不上")
    }

    typeMatch(1)
    typeMatch(10L)
    typeMatch(true)
    typeMatch("scala")

    println("---------------------匹配Array-------------------------")

    def arrayMatch(arr: Any) = arr match {
      case Array(0) => println("只有一个0元素的数组")
      case Array(0, _) => println("以0开头的，拥有两个元素的数组")
      case Array(1, _, 3) => println("以1开头，3结尾，中间为任意元素的三个元素的数组")
      case Array(8, _*) => println("以8开头，N个元素的数组") // _* 标识0个或者多个任意类型的数据
    }

    arrayMatch(Array(0))
    arrayMatch(Array(0, "1"))
    arrayMatch(Array(1, true, 3))
    arrayMatch(Array(8))
    arrayMatch(Array(8, 9, 10, 100))

    println("---------------------匹配List-------------------------")

    def listMatch(list: Any) = list match {
      case 0 :: Nil => println("只有一个0元素的List")
      case 7 :: 9 :: Nil => println("只有7和9元素的List")
      case x :: y :: z :: Nil => println("只有三个元素的List")
      case m :: n if n.length > 0 => println("---------") // 拥有head和tail的数组
      case _ => println("匹配不上")
    }

    listMatch(List(0))
    listMatch(List(7, 9))
    listMatch(List(8, 9, 6666))
    listMatch(List(8, 9, 6666, 333, 555))
    listMatch(List(666))

    println("---------------------匹配元组-------------------------")

    def tupleMatch(tuple: Any) = tuple match {
      case (0, _) => println("元组的第一个元素为0，第二个元素为任意类型的数据，且只有两个元素")
      case (x, m, k) => println("拥有三个元素的元组")
      case (_, "AK47") => println("AK47")
    }

    tupleMatch((0, 1))
    tupleMatch(("2", 1, true))
    tupleMatch((ScalaMatchCase, "AK47"))

    println("---------------------匹配对象-------------------------")

    def objMatch(obj: Any) = obj match {
      case SendHeartBeat(x, y) => println(s"$x $y")
      case CheckTimeOutWorker => println("checkTimeOutWorker")
      case "registerWorker" => println("registerWorker")
    }

    objMatch(SendHeartBeat("appuid11111", System.currentTimeMillis()))
    objMatch(SendHeartBeat("xx", 100L))
    objMatch(CheckTimeOutWorker)
    objMatch("registerWorker")
  }
}

case class SendHeartBeat(id: String, time: Long)

case object CheckTimeOutWorker
