package com.akka.actors

import akka.actor.{Actor, ActorRef, ActorRefFactory, PoisonPill}

class ParentActor(childMaker: ActorRefFactory => ActorRef) extends Actor {
  val child = childMaker(context)
  var result = false

  def receive = {
    case "Hello" => child ! "Hello"
    case "terminate" => child !"terminate"
    case "Hi"   => result = true

  }
}
class ChildActor(parent: ActorRef) extends Actor {
  def receive = {
    case "Hello" => parent ! "Hi"
    case "terminate" => parent !PoisonPill
  }
}