package algo

import argonaut.Parse
import datatypes.{Company, IP}
import org.scalatest.{FlatSpec, PrivateMethodTester}
import org.scalatest.Matchers._

import scala.io.Source

/**
                                      +-----------+
                 +--------------------+164.31.23.7+--------------------+
                 |                    +-----------+                    |
                 |                                                     |
           +-----v------+                                        +-----v------+
       +---+143.1.36.195+-----+                         +--------+195.164.36.1+------+
       |   +------------+     |                         |        +------------+      |
       |                      |                         |                            |
 +-----v----+           +-----v-----+            +------v------+             +-------v------+
 |143.1.36.1|           |164.31.23.1|            |164.31.23.255|             |196.165.37.195|
 +-+-------++           ++--------+-+            +--+------+---+             +---+------+---+
   |       |             |        |                 |      |                     |      |
+--v+     +v--+        +-v-+    +-v-+             +-v-+  +-v-+                 +-v-+  +-v-+
|   |     | A |        |   |    | B |             | A |  |   |                 | A |  |   |
+---+     +---+        +---+    +---+             +---+  +---+                 +---+  +---+

  * Created by Pawel.
  */
class IntervalTreeTest extends FlatSpec with PrivateMethodTester {
  val companiesJson = Source.fromInputStream(getClass.getClassLoader.getResourceAsStream("simpleCompaniesMapping.json")).mkString
  val companies: List[Company] = Parse.decodeOption[List[Company]](companiesJson).get
  val testTree = new IntervalTree(companies)
  testTree.create
  val root = testTree invokePrivate PrivateMethod[Node]('root)()

  "Interval tree" should "sort IP's by natural order and delete duplicates during creation stage" in {
    val decoratedLoadIps = PrivateMethod[Vector[IP]]('loadIps)
    (testTree invokePrivate decoratedLoadIps()) should contain theSameElementsInOrderAs
      Vector(IP(143,1,36,1), IP(143,1,36,195), IP(164,31,23,1), IP(164,31,23,7),
             IP(164,31,23,255), IP(195,164,36,1), IP(196,165,37,195))
  }

  it should "place proper separators in nodes (according to BST algorithm)" in {
    root.separator shouldBe Some(IP(164,31,23,7))
    root.left.separator shouldBe Some(IP(143,1,36,195))
    root.right.separator shouldBe Some(IP(195,164,36,1))
  }

  it should "place companies tags in proper nodes" in {
    root.left.left.right.tagWithPrio shouldBe (Some("A"), 1432)
    root.right.left.left.tagWithPrio shouldBe (Some("A"), 1432)
    root.right.right.left.tagWithPrio shouldBe (Some("A"), 1432)
  }

  it should "store at most one company tag (with lowest prio) in given node" in {
    root.left.right.right.tagWithPrio shouldBe (Some("B"), 14)
  }

  it should "return tag of company with lowest priority which is covering given IP" in {
    testTree.getTagFor(IP(164,31,23,2)) shouldBe Some("B")
  }

  it should "return None if there's no corresponding company for given IP" in {
    testTree.getTagFor(IP(11,31,23,2)) shouldBe None
  }
}
