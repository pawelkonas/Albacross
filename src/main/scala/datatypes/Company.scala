package datatypes

import argonaut.Argonaut._
import argonaut.CodecJson

/**
  * Created by Pawel
  */
case class Company(id: String, priority: Int, ipRanges: List[String]) {
  def getIPRanges(): List[(IP, IP)] = {
    for {
      range <- ipRanges
    } yield range.split('-') match {case Array(from, to) => (fromString(from), fromString(to))}
  }

  def getIps(): List[IP] = {
    getIPRanges.map(range => Vector(range._1, range._2)).flatten
  }

  private def fromString(ipString: String): IP = {
    IP(ipString.split('.').map(_.toInt): _*)
  }
}

object Company {
  implicit def CompanyCodecJson: CodecJson[Company] = CodecJson(
    (c: Company) =>
      ("ips" := c.ipRanges) ->:
      ("priority" := c.priority) ->:
      ("company_id" := c.id) ->:
        jEmptyObject,
    c => for {
      id <- (c --\ "company_id").as[String]
      priority <- (c --\ "priority").as[Int]
      ips <- (c --\ "ips").as[List[String]]
    } yield Company(id, priority, ips))
}