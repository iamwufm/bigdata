package com.bigdata.scala.akkaActor.robot

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

import scala.io.StdIn

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:客户端-Client-Actor-01
 * 启动多个，改下port和Client-Actor-01的值
 */
class ClientActor(host: String, port: Int) extends Actor {
  var serverActorRef: ActorSelection = _ // 服务端的代理对象

  // 在receive方法之前调用
  override def preStart(): Unit = {
    // akka://Server@127.0.0.1:8088
    serverActorRef = context.actorSelection(s"akka://Server@${host}:${port}/user/shanshan")
  }

  // mailBox -> receive
  override def receive: Receive = {
    case "start" => println("牛魔王系列已启动")
    case msg: String => {
      serverActorRef ! ClientMessage(msg) // 把客户端输入的内容发送给 服务端（actorRef）--》服务端的mailbox中 -> 服务端的receive
    }
    case ServerMessage(msg) => println(s"收到服务端消息：$msg")
  }
}

object ClientActor {
  def main(args: Array[String]): Unit = {
    val host = "127.0.0.1"
    val port = 8089

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

    val clientSystem = ActorSystem("client", config)

    // 服务端的地址
    val serverHost = "127.0.0.1"
    val serverPort = 8088
    // 创建dispatch | mailbox
    val actorRef = clientSystem.actorOf(Props(new ClientActor(serverHost, serverPort)), "Client-Actor-02")

    actorRef ! "start" // 自己给自己发送了一条消息 到自己的mailbox => receive

    while (true) {
      val question = StdIn.readLine() // 同步阻塞的， shit
      actorRef ! question // mailbox -> receive
    }
  }
}
