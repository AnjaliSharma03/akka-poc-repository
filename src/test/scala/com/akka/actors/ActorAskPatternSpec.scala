package com.akka.actors

import akka.actor.ActorSystem
import akka.testkit.{DefaultTimeout, ImplicitSender, TestActorRef, TestKit}
import com.akka.actors.ActorAskProtocol.Info
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}

import scala.util.Success

class AskActorTests
  extends TestKit(ActorSystem("AskActorTests"))
    with ImplicitSender
    with DefaultTimeout
    with WordSpecLike
    with BeforeAndAfterAll
    with Matchers {

  override protected def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }
  "Sending Ask Pattern Info(actor) message to AskActor" should {
    "reply back with true" in {
      import akka.pattern._
      val testActor = TestActorRef[AskActor]
      val result = testActor ? Info("actor")
      val Success(reply: Boolean) = result.value.get
      reply shouldBe true
    }
  }
  "Sending Ask Pattern Info(plain) message to AskActor" should {
    "reply back with false" in {
      import akka.pattern._
      val testActor = TestActorRef[AskActor]
      val result = testActor ? Info("plain")
      val Success(reply: Boolean) = result.value.get
      reply shouldBe false
    }
  }
  "The exception message when sending a name to AskActor" should {
    "include the words: is not allowed" in {
      val testActor = TestActorRef[AskActor]
      val exception = the [IllegalStateException] thrownBy {
        testActor.receive("Random")
      }
      exception.getClass shouldEqual classOf[java.lang.IllegalStateException]
      exception.getMessage should be ("Random is not allowed")
    }
  }
}