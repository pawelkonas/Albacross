package algo

import datatypes.IP

/**
  * Created by Pawel
  */
case class Node(var separator: Option[IP] = None,
                var tagWithPrio: (Option[String], Int) = (None, Int.MaxValue),
                var left: Node = null,
                var right: Node = null)

