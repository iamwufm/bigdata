package com.bigdata.scala.akkaActor.robot

/**
 * Date:2023/11/2
 * Author:wfm
 * Desc: 封装信息。样例类实现了Serializable接口
 */

// 服务端发送给客户端的消息格式
case class ServerMessage(msg: String)

// 客户端发送给服务器端的消息格式
case class ClientMessage(msg: String)