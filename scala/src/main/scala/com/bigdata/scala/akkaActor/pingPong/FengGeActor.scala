package com.bigdata.scala.akkaActor.pingPong

import akka.actor.Actor

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:两个人打乒乓球-峰哥
 */
class FengGeActor extends Actor{
  override def receive: Receive = {
    case "start" => println("峰哥：I'm OK!")
    case "啪" => {
      println("峰哥：那必须滴！")
      Thread.sleep(1000)//休息1s
      sender() ! "啪啪" // 回复给他发送消息的人
    }
  }
}
