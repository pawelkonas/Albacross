import java.io.{File, PrintWriter}

import argonaut.Parse
import argonaut._
import Argonaut._
import datatypes.{AugmentedEvent, Company, Event}
import algo.{IntervalTree, Node}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source

/**
  * Created by Pawel.
  */
object Application {
  def main(args: Array[String]) {
    val companiesJson = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("companiesMapping.json")).mkString
    val companies: List[Company] = Parse.decodeOption[List[Company]](companiesJson).get
    val eventsJson = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("events.json")).mkString
    val events: List[Event] = Parse.decodeOption[List[Event]](eventsJson).get


    val tree = new IntervalTree(companies)
    tree.create

    /** //plain Scala approach
    val augmentedEvents: List[AugmentedEvent] = for {
      event <- events
    } yield AugmentedEvent(event.id, event.ipString, tree.getTagFor(event.ip))
    */

    /** SPARK FUN... */
    val conf = new SparkConf().setAppName("AlbacrossPK").setMaster("local[5]")
    conf.set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    conf.registerKryoClasses(Array( classOf[Node], classOf[IntervalTree]))

    val sc = new SparkContext(conf)

    val broadcastedTree = sc.broadcast(tree)
    val distEvents = sc.parallelize(events)
    val result = distEvents.map(e =>{AugmentedEvent(e.id, e.ipString, broadcastedTree.value.getTagFor(e.ip))})
    val augmentedEvents = result.collect

    val writer = new PrintWriter(new File("output.json"))
    writer.write(augmentedEvents.asJson.spaces2)
    writer.close
  }
}
