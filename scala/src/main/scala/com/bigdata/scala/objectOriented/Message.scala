package com.bigdata.scala.objectOriented

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:样例类/样例对象
 */

// 样例类： case class 类名(属性....)
// 支持模式匹配，默认实现了Serializable接口
// 类名的定义必须是驼峰式，属性名称第一个字母小写
case class Message(sender: String, messageContent: String)

// 样例对象：case object 对象名
// 支持模式匹配，默认实现了Serializable接口
// 样例对象不能封装数据
case object CheckHeartBeat

object TestCaseClass{
  def main(args: Array[String]): Unit = {
    // 可以使用 new 关键字创建实例, 也可以不使用
    val message = new Message("刘亦菲","今天晚上不吃饭")
    val message2 = Message("刘亦菲","今天晚上吃饭")
  }
}
