package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:Trait(特质) 的话就可以继承多个，with 实现了多重继承
 */
//编译器在编译会从右往左进行编译检查
object ScalaTraitImpl extends ScalaTrait with FlyTrait with StudentTrait {

  override var name: String = "张三"

  // 定义T的类型
  override type T = Int

  // 如果特质中这个方法没有实现，可以不加override
  override def hello(name: String): Unit = {
    println(s"hello,$name")
  }
  // 重写某个方法，快捷键ctrl+o
  override def small(name: String): Unit = {
    println(s"丁丁 对 $name 哈哈大笑")
  }

  def main(args: Array[String]): Unit = {
    ScalaTraitImpl.hello("lisi")
    ScalaTraitImpl.fly("小猪")
    ScalaTraitImpl.learn(123)
    println(ScalaTraitImpl.animalName)
    println(ScalaTraitImpl.name)
  }
}
