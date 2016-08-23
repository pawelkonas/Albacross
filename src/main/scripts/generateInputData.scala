import java.io.{File, PrintWriter}

import argonaut._
import Argonaut._
import datatypes.{Company, Event}

import scala.util.Random

/**
  * Created by Pawel
  */

val companyIds = ('A' to 'Z').combinations(3).toList.take(1000).map(_.mkString)
val eventIds = ('a' to 'z').combinations(4).toList.take(10000).map(_.mkString)
val priorities = Random.shuffle(1 to 1000)

val listOfCompanies = (0 until 1000).map(idx => generateCompany(idx))

val listOfEvents = for {
  id <- eventIds
} yield generateEvent(id)

writeTo("companiesMapping.json", listOfCompanies.asJson)
writeTo("events.json", listOfEvents.asJson)

/** ######################################## */

def generateCompany(idx: Int): Company = {
  Company(companyIds(idx), priorities(idx), generateIpRanges)
}

def generateIpRanges: List[String] = {
  for {
    no <- (1 to (Random.nextInt(3) + 1)).toList
  } yield generateRange
}

def generateRange: String = {
  val firstIp: List[Int] = (1 to 4).map(e => getIntBetween(150, 180)).toList
  val secondIp: List[Int] = firstIp.map(e => if(Random.nextBoolean) getIntBetween(e, 180) else e)
  firstIp.mkString(".") + "-" + secondIp.mkString(".")
}

def generateEvent(id: String): Event = {
  val ip = (1 to 4).map(e => getIntBetween(150,180)).mkString (".")
  Event(id, ip)
}

def getIntBetween(from: Int, to: Int): Int = from + Random.nextInt(to - from + 1)

def writeTo(filename: String, data: Json) = {
  val writer = new PrintWriter(new File(filename))
  writer.write(data.spaces2)
  writer.close
}