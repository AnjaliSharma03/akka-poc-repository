package com.akka.actors

import akka.actor.{FSM}
import scala.concurrent.duration._

// received events
final case class PowerOn()
final case class PowerOff()

// states
sealed trait AlarmClockState
case object OnAlarm extends AlarmClockState
case object OffAlarm extends AlarmClockState

//data
sealed trait AlarmClockData
case object NoData extends AlarmClockData

class AlarmClockActor extends FSM[AlarmClockState, AlarmClockData] {

  startWith(OffAlarm, NoData)

  when(OffAlarm) {
    case Event(PowerOn, _) =>
      goto(OnAlarm) using NoData
  }

  when(OnAlarm, stateTimeout = 1 second) {
    case Event(PowerOff, _) =>
      goto(OffAlarm) using NoData
    case Event(StateTimeout, _) =>
      println("'On' state timed out, moving to 'OffAlarm'")
      goto(OffAlarm) using NoData
  }

  whenUnhandled {
    case Event(e, s) =>
      log.warning("received unhandled request {} in state {}/{}", e, stateName, s)
      goto(OffAlarm) using NoData
  }

  onTransition {
    case OffAlarm -> OnAlarm => println("Moved from Off to On")
    case OnAlarm -> OffAlarm => println("Moved from On to Off")
  }

  initialize()
}