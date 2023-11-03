package com.bigdata.scala.collection

/**
 * Date:2023/11/3
 * Author:wfm
 * Desc:集合常用方法
 */
object ListMethodTest {
  def main(args: Array[String]): Unit = {

    val list = List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    val nestedList = List(List(1, 2, 3), List(4, 5, 6), List(7, 8, 9))
    val wordList: List[String] = List("hello world", "hello scala", "hello scala")
    val list2 = List(2, 3, 4)


    println("--------------集合计算简单函数-----------------")
    println(list.sum) // 求和 45
    println(list.product) // 求乘积1*2*3..*9 362880
    println(list.max) // 求最大值 9
    println(list.min) // 最最小值 1
    println(list.length) // 元素的个数 9
    println(list.head) // 头部元素 1
    println(list.tail) // 尾部元素 List(2, 3, 4, 5, 6, 7, 8, 9)
    println(list.mkString(",")) // 转字符串 1,2,3,4,5,6,7,8,9
    list.foreach(println) // 遍历每个元素
    println(list.slice(0, 3)) // 截取集合的元素,从0位置开始取3个 List(1, 2, 3)
    println(list.sortBy(x => x)) // 按照元素大小升序排序 List(1, 2, 3, 4, 5, 6, 7, 8, 9)
    println(list.sortBy(x => -x)) // 按元素的大小降序排序 List(9, 8, 7, 6, 5, 4, 3, 2, 1)
    println(list.sortWith((x, y) => x < y)) // 按照元素大小升序排序
    println(list.sortWith(_ > _)) // 按元素的大小降序排序
    println(list.sorted) // 按照元素大小升序排序

    println("--------------映射:map-----------------")
    // 映射:map => 将集合中的每一个元素映射到某一个函数
    println(list.map(_ * 10)) // 每个元素×10 List(10, 20, 30, 40, 50, 60, 70, 80, 90)

    println("--------------过滤：filter-----------------")
    println(list.filter(_ % 2 == 0)) // 取偶数 List(2, 4, 6, 8)

    println("--------------扁平化:flatten-----------------")
    println(nestedList.flatten) // List(1, 2, 3, 4, 5, 6, 7, 8, 9)

    println("--------------扁平化+映射:flatMap => map + flatten-----------------")
    println(wordList.flatMap(_.split(" "))) // List(hello, world, hello, scala, hello, scala)
    println(wordList.map(_.split(" ")).flatten)

    println("--------------分组:group-----------------")
    println(list.groupBy(_ % 2)) // 按奇偶分组 Map(1 -> List(1, 3, 5, 7, 9), 0 -> List(2, 4, 6, 8))
    for (elem <- list.grouped(3)) println(elem) //分三组 List(1, 2, 3) List(4, 5, 6) List(7, 8, 9)

    println("--------------简化（归约）:Reduce-----------------")
    // 简化（归约）:Reduce => 通过指定的逻辑将集合中的数据进行聚合，从而减少数据，最终获取结果
    println(list2.reduce(_ + _)) // 底层是reduceLeft 集合所有的元素相加 2+3+4=9
    println(list2.reduceLeft(_ - _)) // 2-3-4=-5
    println(list2.reduceRight(_ - _)) // (2-(3-4)) = 3 // 先反转集合 [4,3,2]

    println("--------------折叠:Fold -----------------")
    // fold 方法使用了函数柯里化，存在两个参数列表
    // 第一个参数列表为 ： 零值（初始值）
    // 第二个参数列表为： 简化规则
    // 比reduce多了个初始值
    println(list2.fold(1)(_ + _)) // 底层是foldLeft 1+2+3+4 = 10
    println(list2.foldLeft(1)(_ - _)) // 1-2-3-4=-8
    println(list2.foldRight(1)(_ - _)) // 2-(3-(4-1)) = 2 // 先反转集合 [1,4,3,2]

    println("--------------aggregate -----------------")
    val result= list2.aggregate(1)(_ + _, _ + _) // seqOp相当于局部聚合，combOp：全局聚合
    println(result) // 10

    println("--------------两个集合常用操作 -----------------")

    println(list union list2) // 两个集合的合并 List(1, 2, 3, 4, 5, 6, 7, 8, 9, 2, 3, 4)
    println(list intersect list2) // 两个集合的交集 List(2, 3, 4)
    println(list diff list2) // list 与 list2的差集 List(1, 5, 6, 7, 8, 9)
    println(list.zip(list2)) // 拉链 List((1,2), (2,3), (3,4))

  }

}
