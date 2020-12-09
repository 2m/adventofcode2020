// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val numbers = read.lines ! pwd / "day9.txt" map BigInt.apply

val theNumber = test("first number that does not hold the rule") {
  numbers
    .sliding(26)
    .map(_.reverse.toList)
    .collectFirst {
      case candidate :: preamble if preamble.map(candidate - _).toSet.intersect(preamble.toSet).isEmpty => candidate
    }
    .get
}.check(_ == 756008079)

test("encryption weakness") {
  numbers.tails
    .map { tail =>
      tail.scanLeft(Seq.empty[BigInt])(_ :+ _).collectFirst { case set if set.sum >= theNumber => set } flatMap {
        case set if set.sum == theNumber => Some(set.min + set.max)
        case _                           => None
      }
    }
    .collectFirst { case Some(sum) => sum }
}.assert(_ == Some(93727241))

println(Suite.show(test.report()))
