package com.akka.actors

package com.akka.knoldus

import CommunicationProtocol.{CheckNoOfMessages, WorkerFailedException}
import akka.actor.SupervisorStrategy.{Escalate, Restart}
import akka.actor._
import akka.util.Timeout

import scala.language.postfixOps
import akka.pattern._
import akka.routing.DefaultResizer

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future


object ErrorKernalSupervision extends App {
  implicit val timeout = Timeout(5 second)
  val actorSystem = ActorSystem("CommunicationActorSystem")
  val resizer = DefaultResizer(lowerBound = 5, upperBound = 10)
  val communicationActor = actorSystem.actorOf(Props[CommunicationActor], name = "CommunicationActor")

  val noOfMessages: Future[Int] = (communicationActor ? CheckNoOfMessages("Hello")).mapTo[Int]
  for {
    found <- noOfMessages
  } yield (println(s"No of messages found for= $found"))

  val isTerminated = actorSystem.terminate()
}

object CommunicationProtocol {
  case class Message(name: String)

  case class CheckNoOfMessages(name: String)

  case class WorkerFailedException(error: String) extends Exception(error)
}

class CommunicationActor extends Actor with ActorLogging {

  override def supervisorStrategy: SupervisorStrategy =
    OneForOneStrategy(maxNrOfRetries = 3, withinTimeRange = 1 seconds) {
      case _: WorkerFailedException =>
        log.error("Worker failed exception, will restart.")
        Restart

      case _: Exception =>
        log.error("Worker failed, will need to escalate up the hierarchy")
        Escalate
    }

  val workerActor = context.actorOf(Props[CommunicationWorkerActor], name = "CommunicationWorkerActor")

  def receive = {
    case checkCount @ CheckNoOfMessages(name) =>
      log.info(s"Checking no of messages for $name ")
      workerActor forward checkCount
  }
}


class CommunicationWorkerActor extends Actor with ActorLogging {

  @throws[Exception](classOf[Exception])
  override def postRestart(reason: Throwable): Unit = {
    log.info(s"restarting ${self.path.name} because of $reason")
  }

  def receive = {
    case CheckNoOfMessages(name) =>
      val count=findMessages(name)
      log.info(s"Finding count for message  $name = $count")
      context.stop(self)
  }

  def findMessages(name: String): Int = {
    100
  }
}
