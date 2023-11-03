package com.bigdata.scala.akkaActor.pingPong

import akka.actor.{ActorSystem, Props}

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:两个人打乒乓球
 */
object PingPong {

  def main(args: Array[String]): Unit = {
    val nbFactory = ActorSystem("NBFactory") //工厂

    // 通过actorSystem创建ActorRef
    val fGActorRef = nbFactory.actorOf(Props[FengGeActor], "fengGe") // 获取actorRef
    val lGActorRef = nbFactory.actorOf(Props(new LongGeActor(fGActorRef)), "longGe") // 获取actorRef

    // 发送信息
    fGActorRef ! "start"
    lGActorRef ! "start"

  }
}
