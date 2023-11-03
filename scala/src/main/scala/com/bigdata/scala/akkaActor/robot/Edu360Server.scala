package com.bigdata.scala.akkaActor.robot

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Date:2023/11/1
 * Author:wfm
 * Desc:阿里智能机器人-Server-Actor
 */
class Edu360Server extends Actor{
  // 用来接收客户端发送过来的问题
  override def receive: Receive = {
    case "start" => println("老娘已就绪！")
    case ClientMessage(msg) => {
      println(s"收到客户端的消息：$msg")
      msg match {
        case "你叫啥？" => sender()! ServerMessage("铁扇公主")
        case "你是男是女？" => sender()!ServerMessage("老娘是女的")
        case "你有男票嘛？" => sender()!ServerMessage("没有")
        case _ => sender()! ServerMessage("What you say?")// sender()发送端的代理对象，发送到客户端的mailBox中->客户端的receive
      }
    }
  }
}
object Edu360Server{
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 8088

    val config = ConfigFactory.parseString(
      s"""
         |akka{
         |	actor{
         |		provider="akka.remote.RemoteActorRefProvider"
         |		allow-java-serialization=on
         |		}
         |	remote{
         |		enabled-transports=["akka.remote.netty.tcp"]
         |		artery{
         |			transport=tcp
         |			canonical.hostname=$host
         |			canonical.port=$port
         |			}
         |		}
         |}
         """.stripMargin)

    // 指定IP和端口
    val actorSystem = ActorSystem("Server",config)

    val serverActorRef = actorSystem.actorOf(Props[Edu360Server],"shanshan")

    // 给自己发一条信息
    serverActorRef ! "start" // 到自己的mailBox -> receive方法
  }
}
