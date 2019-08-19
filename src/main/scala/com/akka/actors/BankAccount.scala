package com.akka.actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.event.{Logging, LoggingAdapter}

import scala.concurrent.Await
import scala.language.postfixOps
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._


sealed trait Operations
case class GetBalance() extends Operations
case class Withdraw(amount : Double) extends Operations
case class Deposit(amount : Double) extends Operations

class BankAccount extends Actor with ActorLogging{
  var balance: Double = 0
  override def receive: Receive = {
    case w: GetBalance => {
      sender() ! balance
    }
    case w: Withdraw => {
      if (w.amount < balance) {
        balance =balance - w.amount
        log.info(s"After withdrawing ${w.amount}: Balance = $balance")
      }
      else {
        log.info("Low Balance")
      }
    }
    case w: Deposit => {
      balance = balance + w.amount
      log.info(s"After depositing ${w.amount}: Balance = $balance")
    }
  }
}
object BankAccount extends App{
  val system = ActorSystem("BankAccount")
  val actor = system.actorOf(Props[BankAccount],"BankAccount")
  implicit val logger: LoggingAdapter = Logging(system, "Log")

  actor ! Deposit(5000)
  actor ! Withdraw(2000)

  implicit val timeout = Timeout(10 seconds)
  val future = actor ? GetBalance()
  val result = Await.result(future, timeout.duration)
  logger.info(s"Current balance: ${result}")
}
