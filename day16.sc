// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val lines = read.lines ! pwd / "day16.txt"

val (ruleLines, rest) = lines.splitAt(lines.indexWhere(_ == ""))

case class Rule(name: String, ranges: Seq[Range])
val rules = ruleLines.map { case s"$name: $from1-$to1 or $from2-$to2" =>
  Rule(name, Seq(from1.toInt to to1.toInt, from2.toInt to to2.toInt))
}

val (myTicketLines, otherTicketLines) = rest.tail.splitAt(rest.tail.indexWhere(_ == ""))

val tickets = otherTicketLines.drop(2).map(_.split(",").map(_.toInt).toSeq)
val invalid = tickets.flatMap { case ticket =>
  ticket.filterNot(value => rules.exists(_.ranges.exists(_.contains(value))))
}

test("error rate") {
  invalid.sum
}.assert(_ == 21996)

println(Suite.show(test.report()))
