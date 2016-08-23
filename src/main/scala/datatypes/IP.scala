package datatypes

/**
  * Created by Pawel.
  */
case class IP(private val segments: Int*) extends Ordered[IP] {
  require(segments.size == 4, "IP is constructed from FOUR segments")
  require(segments.forall( e=> (0 <= e && e <= 255)), "Each segment should be within [0, 255]")

  override def compare(that: IP): Int = {
    var result = 0
    for(i <- 0 until 4) {
      result = this.segments(i).compareTo(that.segments(i))
      if(result != 0) return result
    }
    result //equal
  }

  override def toString: String = {
    segments.mkString(".")
  }
}

