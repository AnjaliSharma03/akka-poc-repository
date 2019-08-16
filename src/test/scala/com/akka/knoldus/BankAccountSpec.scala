package com.akka.knoldus

import akka.actor.{ActorSystem, Props}
import akka.testkit.{TestKit, TestProbe}
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, Matchers, WordSpecLike}
import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask

class BankAccountSpec extends TestKit(ActorSystem("BankAccount"))
  with WordSpecLike
  with Matchers
  with BeforeAndAfterAll {
  val testProbes = system.actorOf(Props[BankAccount])
  "An actor" must {
    "successfully deposit the money and return current balance" in {
      implicit val timeout = Timeout(10 seconds)
      val testProbe = TestProbe()
      testProbe.send(testProbes,Deposit(2000.00))
      testProbe.send(testProbes,GetBalance())
      val future = testProbes ? GetBalance()
      val result = Await.result(future, timeout.duration)
      testProbe.expectMsg(result)
    }
    "successfully withdraw the money and return current balance" in {
      implicit val timeout = Timeout(10 seconds)
      val testProbe = TestProbe()
      testProbe.send(testProbes,Deposit(2000.00))
      testProbe.send(testProbes,Withdraw(1000.00))
      testProbe.send(testProbes,GetBalance())
      val future = testProbes ? GetBalance()
      val result = Await.result(future, timeout.duration)
      testProbe.expectMsg(result)
    }
    "unable to withdraw the money" in {
      implicit val timeout = Timeout(10 seconds)
      val testProbe = TestProbe()
      testProbe.send(testProbes,Withdraw(3000.00))
      testProbe.send(testProbes,GetBalance())
      val future = testProbes ? GetBalance()
      val result = Await.result(future, timeout.duration)
      testProbe.expectMsg(result)
    }
  }
}