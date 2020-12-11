// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val adapters = read.lines ! pwd / "day10.txt" map (_.toInt)

val deviceAdapterWall = 0 +: adapters.sorted :+ adapters.max + 3

def incr(key: Int, map: Map[Int, Int]) =
  map.updatedWith(key) {
    case Some(count) => Some(count + 1)
    case None        => Some(1)
  }

val distribution = deviceAdapterWall.sliding(2).foldLeft(Map.empty[Int, Int]) {
  case (distr, pair) =>
    val diff = pair.last - pair.head
    distr.updatedWith(diff) {
      case Some(occurence) => Some(occurence + 1)
      case None            => Some(1)
    }
}

test("product of 1 jolt and 3 jolt differences") {
  distribution(1) * distribution(3)
}.assert(_ == 2590)

println(Suite.show(test.report()))

val lengths = deviceAdapterWall.sliding(2).map(s => s.last - s.head)
//println(lengths.toList)
val distr = lengths.sliding(2).foldLeft((0, Map.empty[Int, Int])) {
  case ((count, distr), pair) =>
    pair.toList match {
      case _ :: 1 :: Nil => (count + 1, distr)
      case 1 :: 3 :: Nil => (0, incr(count, distr))
      case 3 :: 3 :: Nil => (0, distr)
    }
}

val combi = distr._2.map {
  case (0, _)     => 1
  case (1, _)     => 1
  case (2, occur) => 2 * occur
  case (3, occur) => 4 * occur
  case (4, occur) => 6 * occur
}

println(combi.product) // 2590
