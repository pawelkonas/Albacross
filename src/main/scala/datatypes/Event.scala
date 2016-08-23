package datatypes

import argonaut.Argonaut._
import argonaut.CodecJson

/**
  * Created by Pawel
  */
case class Event(id: String, ipString: String) {
  def ip: IP = {
    IP(ipString.split('.').map(_.toInt): _*)
  }
}

object Event {
  implicit def EventCodecJson: CodecJson[Event] = CodecJson(
    (e: Event) =>
      ("ip" := e.ipString) ->:
        ("id" := e.id) ->:
        jEmptyObject,
    e => for {
      id <- (e --\ "id").as[String]
      ip <- (e --\ "ip").as[String]
    } yield Event(id, ip))
}
