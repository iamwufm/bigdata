package com.bigdata.scala.collection

/**
 * Date:2023/10/30
 * Author:wfm
 * Desc:定长数组和变长数组
 * 数组：长度不可变，内容可变
 * 数组缓存：长度可变，内容可变
 */
object ArrayTest {

  def main(args: Array[String]): Unit = {
    println("===================定长数组==============================")
    // 1.定义数组
    // 初始化一个长度为5，其所有的元素均为0
    //  相当于调用了数组的 apply 方法，直接为数组赋值
    val arr1 = new Array[Int](5)

    // 将数组转换为数组缓冲，就可以看得到原数组中的内容了
    println(arr1.toBuffer)

    // 2.定义数组
    // 初始化一个长度为1，元素为2的数组
    val arr2 = Array[Int](2)
    println(arr2.toBuffer)

    // 3.定义数组
    // 定义一个长度为3的定长数组
    val arr3 = Array("hadoop","storm","spark")
    // 使用()来访问元素
    println(arr3(2))

    println("===================变长数组（数组缓冲）==============================")
    // 如果想使用数组缓冲，需要导入scala.collection.mutable.ArrayBuffer包
    import scala.collection.mutable.ArrayBuffer
    val ab = ArrayBuffer[Int]()

    // 追加元素
    ab += 1
    ab += (2,3,4,5)
    ab ++= Array(6,7)
    ab ++= ArrayBuffer(8,9)
    println(ab)

    // 在数组某个位置插入元素
    // 在0的位置上插入 -1和0
//    ab.insert(0,-1,0)

    // 删除数组某个位置的元素
    // 在第三个位置，删除2个元素
    ab.remove(3,2)

    println(ab)

  }

}
