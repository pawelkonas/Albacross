package datatypes

import org.scalatest.FlatSpec

/**
  * Created by Pawel.
  */
class IPTest extends FlatSpec {
  "An IP" should "throw exception if we try to create it with less/more than 4 segments" in {
    intercept[IllegalArgumentException] { IP(1,2,3) }
    intercept[IllegalArgumentException] { IP(1,2,3,4,5) }
  }

  it should "throw exception if any segment is out of 0-255 range" in {
    intercept[IllegalArgumentException] {IP(0,0,0,-1)}
    intercept[IllegalArgumentException] {IP(255,255,255,999)}
  }

  it should "be comparable according to natural equality/ordering rules" in {
    assert(IP(128,100,100,100) < IP(128,101,0,0))
    assert(IP(128,100,100,100) === IP(128,100,100,100))
    assert(IP(128,101,0,0) > IP(128,100,255,255))
  }
}
