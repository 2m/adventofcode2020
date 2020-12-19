// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val lines = read.lines ! pwd / "day13.txt"

val departing = lines.head.toInt
val busses = lines.last.split(",")

def waitTime(bus: Int) = bus - (departing % bus)
val bus = busses.filter(_ != "x").map(_.toInt).minBy(waitTime)

test("bus id and wait time product", bus = bus) {
  bus * waitTime(bus)
}.assert(_ == 2092)

case class Bus(id: Int, offset: Int)
object Bus {
  def fromTuple(t: (String, Int)) = Bus(t._1.toInt, t._2)
}

val allBusses = busses.zipWithIndex.filter(_._1 != "x").map(Bus.fromTuple).toList
val firstBus :: rest = allBusses

val firstBusDepartures = LazyList.unfold(BigInt(0))(sum => Some((sum + firstBus.id, sum + firstBus.id)))
def notDepartSubsequently(from: BigInt) = !rest.forall(b => (from + b.offset) % b.id == 0)

println(Suite.show(test.report()))

//println(firstBusDepartures.map { a => println(a); a }.dropWhile(notDepartSubsequently).head)
