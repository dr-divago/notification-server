package domain

sealed trait Event {
  def eventType: String
}

case class EventA(data: String, eventType: String = "EventA") extends Event
case class EventB(data: String, eventType: String = "EventB") extends Event

object Command {
  
}
