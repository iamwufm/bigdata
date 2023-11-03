package com.bigdata.scala.collection

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:Seq序列--可变--ListBuffer
 */
object MutListTest {

  def main(args: Array[String]): Unit = {
    // 导包
    import scala.collection.mutable.ListBuffer

    // 构建一个可变列表，初始化有三个元素1，2，3
    val list0 = ListBuffer[Int](1, 2, 3)

    // 创建一个空的可变列表
    val list1 = new ListBuffer[Int]

    // 向list1中追加元素，注意:没有生成新的集合
    list1 += 4
    list1.append(5)

    // 将list0和list1合并成一个新的ListBuffer。注意：生成一个新的集合
    val list2 = list0 ++ list1

    // 将元素追加到list0的后面生成一个新的集合
    val list3 = list0 :+ 5

  }

}
