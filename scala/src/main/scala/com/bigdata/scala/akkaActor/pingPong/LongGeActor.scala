package com.bigdata.scala.akkaActor.pingPong

import akka.actor.{Actor, ActorRef}

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc: 两个人打乒乓球-龙哥
 */
class LongGeActor(val fg: ActorRef) extends Actor {
  // 接收消息的
  override def receive: Receive = {
    case "start" => {
      println("龙哥：I'm OK!")
      fg ! "啪" // 给fg发消息
    }
    case "啪啪" => {
      println("龙哥：你真猛！！！")
      Thread.sleep(1000) // 休息1s
      fg ! "啪" // 给fg发消息
    }
  }
}
