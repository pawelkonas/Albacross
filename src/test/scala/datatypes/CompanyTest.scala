package datatypes

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

/**
  * Created by Pawel.
  */
class CompanyTest extends FlatSpec {
  val testCompany = new Company("test", 100 ,
                    List("100.98.200.10-100.98.210.198",
                         "255.255.255.10-255.255.255.255"))

  "Company" should "allow to return ranges as list of IP tuples" in {
    testCompany.getIPRanges should contain theSameElementsAs
      List((IP(100,98,200,10), IP(100,98,210,198)),
           (IP(255,255,255,10), IP(255,255,255,255)))
  }

  it should "allow also to get all ranges 'endpoints' as flat list of IP's" in {
    testCompany.getIps should contain theSameElementsAs
      List(IP(100,98,200,10), IP(100,98,210,198),
           IP(255,255,255,10), IP(255,255,255,255))
  }
}
