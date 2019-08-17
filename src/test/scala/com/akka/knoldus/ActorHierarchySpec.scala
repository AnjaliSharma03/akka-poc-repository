package com.akka.knoldus

import org.scalatest.MustMatchers
import akka.actor.{ ActorSystem, Props, ActorRefFactory }
import akka.testkit.{ TestKit, TestProbe, ImplicitSender }
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }

class ActorHierarchySpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender
  with FlatSpecLike
  with BeforeAndAfterAll
  with MustMatchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "Parent" should
    "send ping message to child when receive ping message" in {
    val child = TestProbe()
    val childMaker = (_: ActorRefFactory) => child.ref
    val parent = system.actorOf(Props(new Parent(childMaker)))

    parent ! "ping"

    child.expectMsg("ping")
  }

  "Child Actor" should
    "send pong message when receive ping message" in {
    val parent  = TestProbe()
    val child = system.actorOf(Props(new Child(parent.ref)))

    child ! "ping"

    parent.expectMsg("pong")
  }
}