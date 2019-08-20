package com.akka.actors

import java.util.concurrent.TimeUnit

import akka.actor._
import akka.util.Timeout

object DefaultDispatcher extends App {

  val system=ActorSystem("AkkaDispatcherSystem")
  implicit val timeout = Timeout(1, TimeUnit.MINUTES)

  val defaultDispatcherConfig=system.settings.config.getConfig("akka.actor.default-dispatcher")
  println(s"akka default dispatcher config=$defaultDispatcherConfig")
  println("\nStep 3: Akka default dispatcher type")
  val dispatcherType = defaultDispatcherConfig.getString("type")
  println(s"$dispatcherType")

  println("\nStep 4: Akka default dispatcher throughput")
  val dispatcherThroughput = defaultDispatcherConfig.getString("throughput")
  println(s"$dispatcherThroughput")

  println("\nStep 5: Akka default dispatcher minimum parallelism")
  val dispatcherParallelismMin = defaultDispatcherConfig.getInt("fork-join-executor.parallelism-min")
  println(s"$dispatcherParallelismMin")

  println("\nStep 6: Akka default dispatcher maximum parallelism")
  val dispatcherParallelismMax = defaultDispatcherConfig.getInt("fork-join-executor.parallelism-max")
  println(s"$dispatcherParallelismMax")
  implicit val executionContext = system.dispatchers.lookup("fixed-thread-pool")
  val isTerminate=system.terminate()
}


