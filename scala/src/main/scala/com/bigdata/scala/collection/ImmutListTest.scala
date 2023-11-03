package com.bigdata.scala.collection

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:Seq序列--不可变--List
 */
object ImmutListTest {

  def main(args: Array[String]): Unit = {
    // 创建一个不可变的集合
    val list1 = List(1, 2, 3)

    // 在list1的前面插入新的元素，生成一个新的List
    val list2 = 0 :: list1
    val list3 = list1.::(0)
    val list4 = 0 +: list1
    val list5 = list1.+:(0)

    // 将一个元素添加到list1的后面生成一个新的List
    val list6 = list1 :+ 3

    val list0 = List(4, 5, 6)
    // 将两个元素合并成一个新的List
    val list7 = list1 ++ list0
    // 将list0插入到list1后面生成一个新的集合
    val list8 = list1 ++: list0
    // 将list0插入到list1前面生成一个新的集合
    val list9 = list1.:::(list0)

    // 创建一个空集合
    val list10 = Nil

    val list11 = 2 :: 3 :: Nil // List[Int] = List(2, 3)

  }

}
