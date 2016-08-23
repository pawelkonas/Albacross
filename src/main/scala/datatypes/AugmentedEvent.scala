package datatypes

import argonaut.Argonaut._
import argonaut.CodecJson

/**
  * Created by Pawel.
  */
case class AugmentedEvent(id: String, ip: String, company_id: Option[String])

object AugmentedEvent {
  implicit def AugmentedEventCodecJson: CodecJson[AugmentedEvent] = CodecJson(
    (ae: AugmentedEvent) =>
      ae.company_id.map("company_id" := _) ->?:
        ("ip" := ae.ip) ->:
        ("id" := ae.id) ->:
        jEmptyObject,
    ae => for {
      id <- (ae --\ "id").as[String]
      ip <- (ae --\ "ip").as[String]
      company_id <- (ae --\ "company_id").as[String]
    } yield AugmentedEvent(id, ip, Some(company_id)))
}
