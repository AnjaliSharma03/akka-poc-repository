package com.akka.actors

import akka.actor.ActorSystem
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest._
import akka.testkit.TestFSMRef
import com.akka.fsm.{AlarmClockActor, NoData, OffAlarm, OnAlarm, PowerOn}

import scala.concurrent.duration._

class AlarmClockFSMActorTests
  extends TestKit(ActorSystem("MySpec"))
    with ImplicitSender
    with WordSpecLike
    with BeforeAndAfterAll
    with Matchers {

  override def afterAll {
    TestKit.shutdownActorSystem(system)
  }

  "An AlarmClockActor " must {
    "start in the 'Off' state" in {
      val fsm = TestFSMRef(new AlarmClockActor())
      assert(fsm.stateName == OffAlarm)
      assert(fsm.stateData == NoData)
    }
  }

  "An AlarmClockActor that starts with 'Off' " must {
    "should transition to 'On' when told to by the test" in {
      val fsm = TestFSMRef(new AlarmClockActor())
      fsm.setState(stateName = OnAlarm)
      assert(fsm.stateName == OnAlarm)
      assert(fsm.stateData == NoData)
    }
  }

  "An AlarmClockActor that starts with 'Off' " must {
    "should transition to 'On' when sent a 'PowerOn' message" in {
      val fsm = TestFSMRef(new AlarmClockActor())
      fsm ! PowerOn
      assert(fsm.stateName == OnAlarm)
      assert(fsm.stateData == NoData)
    }
  }

  "An AlarmClockActor that stays 'On' for more than 1 second " must {
    "should transition to 'Off' thanks to the StateTimeout" in {
      val fsm = TestFSMRef(new AlarmClockActor())
      fsm ! PowerOn
      awaitCond(fsm.stateName == OffAlarm, 1200 milliseconds, 100 milliseconds)
    }
  }
}

