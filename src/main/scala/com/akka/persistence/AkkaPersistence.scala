package com.akka.persistence

import akka.persistence._

case class Event(data: String)
case class Command(data: String)

class DemoPersistentActor extends PersistentActor {

  //note : This is  mutable
  var state = DemoState()

  def updateState(event: Event): Unit =
    state = state.updated(event)

  def numEvents =
    state.size

  val receiveRecover: Receive = {
    case evt: Event => updateState(evt)
    case SnapshotOffer(_, snapshot: DemoState) => {
      println(s"offered state = $snapshot")
      state = snapshot
    }
  }

  val receiveCommand: Receive = {
    case Command(data) =>
      persist(Event(s"${data}-${numEvents}"))(updateState)
      persist(Event(s"${data}-${numEvents + 1}")) { event =>
        updateState(event)
        context.system.eventStream.publish(event)
      }
    case "snap"  => saveSnapshot(state)
    case SaveSnapshotSuccess(metadata) =>
      println(s"SaveSnapshotSuccess(metadata) :  metadata=$metadata")
    case SaveSnapshotFailure(metadata, reason) =>
      println("""SaveSnapshotFailure(metadata, reason) :
        metadata=$metadata, reason=$reason""")
    case "print" => println(state)
//    case "boom"  => throw new Exception("boom")
  }

  override def persistenceId = "demo-persistent-actor-1"
}