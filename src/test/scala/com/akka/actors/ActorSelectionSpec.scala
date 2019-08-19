package com.akka.actors

import org.scalatest.MustMatchers
import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import com.akka.actors.ActorSelectionProtocol.{GetCount, Increment}
import org.scalatest.{BeforeAndAfterAll, FlatSpecLike}

class ActorSelectionSpec extends TestKit(ActorSystem("test-system"))
  with FlatSpecLike
  with ImplicitSender
  with BeforeAndAfterAll
  with MustMatchers {

  override def afterAll = {
    TestKit.shutdownActorSystem(system)
  }

  "ActorSelection Actor" should "handle GetCount message with using TestProbe" in {
    val sender = TestProbe()
    val counter = system.actorOf(Props[ActorSelection])

    sender.send(counter, Increment)
    sender.send(counter, GetCount)

    val state = sender.expectMsgType[Int]
    state must equal(1)
  }

  it should "handle GetCount message" in {
    val counter = system.actorOf(Props[ActorSelection])

    counter ! GetCount

    expectMsg(0)
  }

  it should "handle sequence of message" in {
    val counter = system.actorOf(Props[ActorSelection])

    counter ! Increment
    counter ! Increment
    counter ! Increment
    counter ! GetCount

    expectMsg(3)
  }
}