package com.akka.knoldus

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}

/*object AkkaLifecycle extends App {
  val system = ActorSystem("ActorLifecycle")
  val actor = system.actorOf(Props[ActorLifecycle],"Lifecycle")
  actor ! Info("Something")
  Thread.sleep(5000)
  system.terminate
}
case class Info(someString: String)
class ActorLifecycle extends Actor with ActorLogging {

  override def preStart(): Unit = log.info("preStart")

  override def postStop(): Unit = log.info("postStop")

  override def preRestart(reason: Throwable, message: Option[Any]): Unit = log.info("preRestart")

  override def postRestart(reason: Throwable): Unit = log.info("postRestart")

  val childActorLifecycle = context.actorOf(Props[ChildActorLifecycle], name = "childActorLifecycle")

  override def receive: Receive = {
    case msg @ Info(name) =>
      log.info(s"Found $name in parent")
      childActorLifecycle forward msg
      throw new Exception
  }
}
class ChildActorLifecycle extends Actor with ActorLogging {
  override def preStart(): Unit = log.info("preStart")

  override def postStop(): Unit = log.info("postStop")

  override def receive: Receive = {
    case Info(name) =>
      log.info(s"Found $name in child")
  }
}
*/
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
      //sender() ! msg
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