package com.akka.persistence

import akka.actor.{ActorSystem, Props}
import scala.io.StdIn

object Demo extends App {

  //create the actor system
  val system = ActorSystem("PersistenceSystem")

  val persistentActor =
    system.actorOf(Props(classOf[DemoPersistentActor]),
      "demo-persistent-actor-1")

 // persistentActor ! "print"
  persistentActor ! Command("foo")
  persistentActor ! Command("baz")
 // persistentActor ! "boom"
  persistentActor ! Command("bar")
  persistentActor ! "snap"
  persistentActor ! Command("buzz")
  persistentActor ! "print"


  StdIn.readLine()

  //shutdown the actor system
  system.terminate()

  StdIn.readLine()
}

