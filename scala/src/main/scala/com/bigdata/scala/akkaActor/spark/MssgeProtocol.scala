package com.bigdata.scala.akkaActor.spark

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc:
 */

// worker向master注册自己（id,内存，核数）
case class RegisterWorkerInfo(workId: String, ram: Int, core: Int)

// worker 给自己发送消息，告诉自己说要开始周期性的向master发送心跳消息
case object SendHeartBeat

// worker给master发送心跳信息
case class HearBeat(id: String)

// 存储worker信息的类
class WorkerInfo(val workId: String, ram: Int, core: Int){
  // 最后的心跳时间
  var lastHeartBeatTime: Long = _
}

// master向worker发送注册成功消息
case object RegisteredWorkerInfo

//master给自己发送一个检查超时worker的信息,并启动一个调度器，周期新检测删除超时worker
case object CheckTimeOutWorker

// master发送给自己的消息，删除超时的worker
case object RemoveTimeOutWorker
