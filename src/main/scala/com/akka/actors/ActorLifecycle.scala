package com.akka.actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

object RootActor extends App{
  val system = ActorSystem("RootActor")
  val actor = system.actorOf(Props[RootActor],"RootActor")
  actor ! "hello"
  system.terminate()
}
class RootActor extends Actor with ActorLogging {
  def receive = {
    case msg =>
      log.info(s"Message received: $msg")
      10/0
  }
  override def preStart(){
    super.preStart()
    log.info("preStart method is called")
  }
  override def postStop(){
    super.postStop()
    log.info("postStop method is called")
  }
  override def preRestart(reason:Throwable, message: Option[Any]){
    super.preRestart(reason, message)
    log.info("preRestart method is called")
    log.info(s"Reason: $reason")
  }
  override def postRestart(reason:Throwable){
    super.postRestart(reason)
    log.info("postRestart is called")
    log.info(s"Reason: $reason")
  }
}