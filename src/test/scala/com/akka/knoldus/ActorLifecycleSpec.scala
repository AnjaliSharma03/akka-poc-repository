package com.akka.knoldus

import org.scalatest.MustMatchers
import akka.actor.{ ActorSystem, Props, ActorRefFactory }
import akka.testkit.{ TestKit, TestProbe, ImplicitSender }
import org.scalatest.{ FlatSpecLike, BeforeAndAfterAll }

class ActorLifecycleSpec extends TestKit(ActorSystem("test-system"))
  with ImplicitSender
  with FlatSpecLike
  with BeforeAndAfterAll
  with MustMatchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }
  "ActorSelection Actor" should "handle GetCount message with using TestProbe" in {
    val sender = TestProbe()
    val counter = system.actorOf(Props[RootActor])

   // sender.send(counter,RootActor.preStart )
    //sender.send(counter)

   // val state = sender.expectMsgType[String]
    //state must equal()
  }


}