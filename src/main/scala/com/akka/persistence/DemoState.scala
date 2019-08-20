package com.akka.persistence

case class DemoState(events: List[String] = Nil) {
  def updated(event: Event): DemoState = copy(event.data :: events)
  def size: Int = events.length
  override def toString: String = events.reverse.toString
}