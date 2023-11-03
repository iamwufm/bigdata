package com.bigdata.scala.akkaActor.helloActor
import akka.actor.{Actor, ActorSystem, Props}

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc: 向自己发送信息
 * 两个重点：
 * 获取actorRef(相当于代理对象)，发送信息 actorRef ！ 信息
 * 接受信息receive
 *
 */
// 继承actor
class HelloActor extends Actor{

  // 接收消息的
  override def receive: Receive = {
    // 接收消息并处理
    case "你好帅" => println("净说实话，我喜欢你这种人！")
    case "丑" => println("滚犊子")
    case "stop" => {
      context.stop(self) // 停止自己的actorRef
      context.system.terminate() // 关闭ActorSystem
    }
  }
}
object HelloActor {

  private val nbFactory = ActorSystem("NBFactory") //工厂
  private val helloActorRef = nbFactory.actorOf(Props[HelloActor],"helloActor") // 获取actorRef

  def main(args: Array[String]): Unit = {
    // 给自己发送信息
    helloActorRef ! "你好帅"
    helloActorRef ! "丑"
    helloActorRef ! "stop"
  }
}
