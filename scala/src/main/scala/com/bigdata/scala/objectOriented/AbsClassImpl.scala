package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:在scala中第一个继承抽象类或者特质，只能使用关键字extends
 * 如果想继承多个特质的话，可以在extends之后使用with关键字
 */
object AbsClassImpl extends AbsClass with FlyTrait {
  override def eat(food: String): String = {
    s"$food 炒着吃"
  }

  def main(args: Array[String]): Unit = {
    AbsClassImpl.swimming("漂着")
  }
}
