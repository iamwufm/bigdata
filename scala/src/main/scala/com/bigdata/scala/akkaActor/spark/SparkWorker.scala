package com.bigdata.scala.akkaActor.spark

import java.util.UUID

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:Spark-Worker
 *1.Worker会向Master注册自己（内存，核数，...）
 *2.Master收到Worker的注册信息之后，会告诉Worker已注册成功
 *3. Worker收到Master的注册成功消息之后，会定期向master汇报自己的状态，也就向Master报活（心跳）
 *4.Master收到Worker的心跳信息之后，定期的更新Worker的状态，如心跳发送时间
 *5.Master会定期监测发送过来的心跳时间和当前时间的差，如果大于5分钟，则认为Worker已死。然后Master再分任务的时候就不会给该Worker下发任务
 */
class SparkWorker(masterUrl: String) extends Actor {

  var masterProxy: ActorSelection = _
  val workId = UUID.randomUUID().toString

  // 在执行receive前执行
  override def preStart(): Unit = {
    // 获取Master的actorRef
    masterProxy = context.actorSelection(masterUrl)
  }

  // 接收消息
  override def receive: Receive = {
    case "started" => {
      // 自己已就绪
      // 1.Worker会向Master注册自己（id,内存，核数，...）
      masterProxy ! RegisterWorkerInfo(workId, 32 * 1024, 8)
    }
    // 3. Worker收到Master的注册成功消息之后，会定期向master汇报自己的状态，也就向Master报活（心跳）
    case RegisteredWorkerInfo => {
      import context.dispatcher
      import scala.concurrent.duration._ // 导入时间单位
      // master 启动一个定时器，每隔1.5秒给自己发一条信息
      context.system.scheduler.scheduleAtFixedRate(0 millis, 6000 millis, self, SendHeartBeat)
    }
    case SendHeartBeat => {
      // 开始向master发送心跳了
      println(s"------- $workId 发送心跳-------")
      masterProxy ! HearBeat(workId) // 此时master将会收到心跳信息
    }
  }
}

object SparkWorker {

  def main(args: Array[String]): Unit = {

    // 检验参数
    if(args.length != 4) {
      println(
        """
          |请输入参数：<host> <port> <workName> <masterURL>
                """.stripMargin)
      sys.exit() // 退出程序
    }

    val host = args(0)// 如 "127.0.0.1"
    val port = args(1) // 如 8089
    val workName = args(2) // 如 "Worker"
    val masterURL = args(3) // akka://sparkMaster@127.0.0.1:8088/user/Master

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

    val actorSystem = ActorSystem("sparkWorker", config)

    // 创建自己的actorRef
    val workerActorRef = actorSystem.actorOf(Props(new SparkWorker(masterURL)), workName)

    // 给自己发送一个以启动的消息，标识自己已经就绪了
    workerActorRef ! "started"
  }
}

