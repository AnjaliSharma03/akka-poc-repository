package com.akka.knoldus

import akka.actor.{Actor, ActorRef, ActorRefFactory}

class Parent(childMaker: ActorRefFactory => ActorRef) extends Actor {
  val child = childMaker(context)
  var result = false

  def receive = {
    case "ping" => child ! "ping"
    case "pong"   => result = true
  }
}
class Child(parent: ActorRef) extends Actor {
  def receive = {
    case "ping" => parent ! "pong"
  }
}