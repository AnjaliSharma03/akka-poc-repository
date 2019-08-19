package com.akka.actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import com.akka.actors.ActorSelectionProtocol.{GetCount, Increment}

object ActorSelectionProtocol{
  case class Increment()
  case class GetCount()
}
class ActorSelection extends Actor with ActorLogging{
  var int = 0
  override def receive: Receive = {
    case Increment =>
      int += 1
      log.info(s"$int")
    case GetCount =>
      sender() ! int
  }
}
object ActorSelection extends App{
  val system = ActorSystem("ActorSelection")
  val actor_1 = system.actorOf(Props[ActorSelection],"ActorSelection-1")
  val actor_2 = system.actorOf(Props[ActorSelection],"ActorSelection-2")
  actor_1 ! Increment
  system.actorSelection("/user/ActorSelection-1") ! Increment
  system.actorSelection("/user/*") ! Increment
}