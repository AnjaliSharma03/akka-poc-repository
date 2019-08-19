package com.akka.actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.pattern._
import akka.util.Timeout
import ActorAskProtocol.{CheckInfo, Info}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

object ActorAskProtocol {
  case class Info(name: String)
  case class CheckInfo(name: String)
}

class AskActor extends Actor with ActorLogging {

  def receive = {
    case Info(name) if name == "actor" =>
      log.info(s"Found valid $name")
      sender ! true

    case CheckInfo(name) if name == "actor"=>
      getNumber(name).pipeTo(sender)

    case Info(name) =>
      log.info(s"$name is not supported")
      sender ! false

    case name @ _ =>
      log.info(s"$name is not allowed.")
      throw new IllegalStateException(s"$name is not allowed")
  }

  def getNumber(name: String): Future[Int] = Future {
    100
  }
}

object AskActor extends App {
  val system = ActorSystem("AskActorSystem")
  val askActor = system.actorOf(Props[AskActor], name = "AskActor")
  implicit val timeout = Timeout(5 second)

  val actorFound: Future[Boolean] = (askActor ? Info("actor")).mapTo[Boolean]
  for {
    found <- actorFound
  } yield println(s"Found = $found")

  val getCheckInfo: Future[Int] = (askActor ? CheckInfo("actor")).mapTo[Int]
  for {
    found <- getCheckInfo
  } yield println(s"Info = $found")
  system.terminate()
}