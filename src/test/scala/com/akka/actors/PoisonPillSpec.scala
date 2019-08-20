package com.akka.actors

import org.scalatest.MustMatchers
import akka.actor.{ActorRefFactory, ActorSystem, PoisonPill, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike}

class PoisonPillSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender
  with FlatSpecLike
  with BeforeAndAfterAll
  with MustMatchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Parent" should
    "send Hello message to child when receive Hello message" in {
    val child = TestProbe()
    val childMaker = (_: ActorRefFactory) => child.ref
    val parent = system.actorOf(Props(new ParentActor(childMaker)))

    parent ! "Hello"

    child.expectMsg("Hello")
  }

  "Child Actor" should
    "send Hi message when receive Hello message" in {
    val parent  = TestProbe()
    val child = system.actorOf(Props(new ChildActor(parent.ref)))

    child ! "Hello"

    parent.expectMsg("Hi")
  }

  "Parent" should
    "send terminate message to child, when receive terminate message" in {
    val child = TestProbe()
    val childMaker = (_: ActorRefFactory) => child.ref
    val parent = system.actorOf(Props(new Parent(childMaker)))

    parent ! "terminate"
    child.expectMsg("terminate")

  }

  "Parent" should
    "send PoisonPill message to child when receive terminate message" in {
    val child = TestProbe()
    val childMaker = (_: ActorRefFactory) => child.ref
    val parent = system.actorOf(Props(new ParentActor(childMaker)))

    parent ! "terminate"
    child.expectTerminated(parent)

  }
}