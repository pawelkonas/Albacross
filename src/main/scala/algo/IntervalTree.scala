package algo

import datatypes.{Company, IP}

/**
  * Created by Pawel.
  */
class IntervalTree(companies: List[Company]) extends Serializable {
  private val root: Node = Node()
  private val MIN_IP: IP = IP(0, 0, 0, 0)
  private val MAX_IP: IP = IP(255, 255, 255, 255)

  def create: Unit = {
    fillSeparators(loadIps)
    fillCompanies
  }

  def getTagFor(ip: IP): Option[String] = {
    var currentTuple = root.tagWithPrio
    var node = root

    while(node != null) {
      val candidateTuple = node.tagWithPrio
      if(candidateTuple._2 < currentTuple._2) currentTuple = candidateTuple
      node = node.separator match {
        case None => node.left //doesn't matter here
        case Some(sep) => if(ip < sep) node.left else node.right }
    }
    currentTuple._1
  }

  /** internal guts */

  private def loadIps: Vector[IP] = {
    val allIPs = for {
      company <- companies
    } yield company.getIps

    allIPs.flatten.toVector.distinct.sorted
  }

  private def fillSeparators(ips: Vector[IP]): Unit = {
    def fill(root: Node, from: Int, to: Int): Unit = {
      val mid: Int = (from + to)/2
      root.separator = Some(ips(mid))
      root.left = Node()
      root.right = Node()
      if(to - from > 1) {
        fill(root.left, from, mid)
        fill(root.right, mid + 1, to)
      }
    }
    fill(root, 0, ips.length - 1)
  }

  private def fillCompanies: Unit = {
    def insertToTree(c: Company, fromIP: IP, toIP: IP, min: IP, max: IP, node: Node): Unit = {
      if(node == null) return
      if(fromIP <= min && max <= toIP)
        setCompany(node, c)
      else {
        val separator = node.separator.get
        if(fromIP < separator) insertToTree(c, fromIP, toIP, min, separator, node.left)
        if(separator < toIP) insertToTree(c, fromIP, toIP, separator, max, node.right)
      }
    }

    def setCompany(node: Node, c: Company): Unit = {
      node.tagWithPrio match {
        case (None, _) => node.tagWithPrio = (Some(c.id), c.priority)
        case (Some(tag), currentPrio) => if(currentPrio > c.priority) node.tagWithPrio = (Some(c.id), c.priority)
      }
    }

    for {
      c <- companies
      (fromIP, toIP) <- c.getIPRanges
    } insertToTree(c, fromIP, toIP, MIN_IP, MAX_IP, root)
  }
}


