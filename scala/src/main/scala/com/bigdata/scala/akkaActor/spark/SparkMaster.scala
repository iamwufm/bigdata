package com.bigdata.scala.akkaActor.spark

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:Spark-Master
 *1.Worker会向Master注册自己（内存，核数，...）
 *2.Master收到Worker的注册信息之后，会告诉Worker已注册成功
 *3. Worker收到Master的注册成功消息之后，会定期向master汇报自己的状态，也就向Master报活（心跳）
 *4.Master收到Worker的心跳信息之后，定期的更新Worker的状态，如心跳发送时间
 *5.Master会定期监测发送过来的心跳时间和当前时间的差，如果大于5分钟，则认为Worker已死。然后Master再分任务的时候就不会给该Worker下发任务
 *
 * 打包放入服务器运行：
 * java -cp 包名.jar 类名 参数1 参数2
 * (在pom文件指定了类名。可以采用这个方式)
 * java -jar 包名.jar  参数1 参数2
 */
class SparkMaster extends Actor {

  // 存储worker的信息的
  val id2WorkerInfo = collection.mutable.HashMap[String, WorkerInfo]()

  //    override def preStart(): Unit = {
  //        context.system.scheduler.schedule(0 millis, 6000 millis, self, RemoveTimeOutWorker)
  //    }

  // 接收消息
  override def receive: Receive = {
    // 收到Worker发过来的注册信息
    case RegisterWorkerInfo(workId, ram, core) => {
      // 将worker的信息存储起来，存储到HashMap
      if (!id2WorkerInfo.contains("workId")) {
        val workerInfo = new WorkerInfo(workId, ram, core)
        id2WorkerInfo += ((workId, workerInfo))

        // 2.Master收到Worker的注册信息之后，会告诉Worker已注册成功
        sender() ! RegisteredWorkerInfo
      }
    }

    // 4.Master收到Worker的心跳信息之后，定期的更新Worker的状态，如心跳发送时间
    case HearBeat(workId) => {
      // master收到worker的心跳消息之后，更新woker的上一次心跳时间
      val workerInfo = id2WorkerInfo(workId)
      // 更改心跳时间
      val currentTime = System.currentTimeMillis()
      workerInfo.lastHeartBeatTime = currentTime
    }
    // 5.Master会定期监测发送过来的心跳时间和当前时间的差，如果大于5分钟，则认为Worker已死。然后Master再分任务的时候就不会给该Worker下发任务
    case CheckTimeOutWorker => {
      import context.dispatcher
      import scala.concurrent.duration._ // 导入时间单位
      // master 启动一个定时器，每隔6秒给自己发一条信息
      context.system.scheduler.scheduleAtFixedRate(0 millis, 6000 millis, self, RemoveTimeOutWorker)
    }
    case RemoveTimeOutWorker => {
      // 将hashMap中的所有的value都拿出来，查看当前时间和上一次心跳时间的差 3秒
      val workerInfos = id2WorkerInfo.values
      val currentTime = System.currentTimeMillis()
      //  过滤超时的worker
      workerInfos
        .filter(workerInfo => currentTime - workerInfo.lastHeartBeatTime > 3000)
        .foreach(workerInfo => id2WorkerInfo.remove(workerInfo.workId))

      println(s"-----还剩 ${id2WorkerInfo.size} 存活的Worker-----")
    }
  }
}

object SparkMaster {
  def main(args: Array[String]): Unit = {
    // 检验参数
    if (args.length != 3) {
      println(
        """
          |请输入参数：<host> <port> <masterName>
          """.stripMargin)
      sys.exit() // 退出程序
    }

    val host = args(0) // 如 "127.0.0.1"
    val port = args(1) // 如 8088
    val masterName = args(2) // 如 "Master"

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

    val actorSystem = ActorSystem("sparkMaster", config)

    val masterActorRef = actorSystem.actorOf(Props[SparkMaster], masterName)

    // 自己给自己发送一个消息，去启动一个调度器，定期的检测HashMap中超时的worker
    masterActorRef ! CheckTimeOutWorker
  }
}
