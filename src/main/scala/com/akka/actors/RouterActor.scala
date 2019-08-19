package com.akka.actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.routing.{BroadcastPool, RoundRobinPool, ScatterGatherFirstCompletedPool}
import scala.concurrent.duration._

class RouterActor extends Actor with ActorLogging {
  override def receive = {
    case msg: String => log.info(s" I am ${self.path.name}")
    case _ => log.info("Unknown message ")
  }
}
object RoundRobinPoolApp extends App {
  val actorSystem = ActorSystem("Akka-RoundRobin-Example")
  val roundRobinRouter =
    actorSystem.actorOf(RoundRobinPool(2).props(Props[RouterActor]))
  for (i <- 1 to 4) {
    roundRobinRouter ! s"Hello $i"
  }
  val broadcastRouter =
    actorSystem.actorOf(BroadcastPool(3).props(Props[RouterActor]))
  broadcastRouter ! "hello"
}