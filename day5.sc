// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val seats = read.lines ! pwd / "day5.txt" map toSeat

case class Seat(row: Int, column: Int) {
  def id = row * 8 + column
}

def toSeat(s: String) = {
  val row = s
    .take(7)
    .map {
      case 'F' => "0"
      case 'B' => "1"
    }
    .mkString

  val column = s
    .drop(7)
    .map {
      case 'L' => "0"
      case 'R' => "1"
    }
    .mkString

  Seat(Integer.parseInt(row, 2), Integer.parseInt(column, 2))
}

test("max seat id") {
  seats.map(_.id).max
}.assert(_ == 906)

val all = (0 to 127).flatMap(row => (0 to 7).map(Seat(row, _))) toSet
val freeIds = all.diff(seats.toSet).map(_.id)

test("my seat id") {
  freeIds.filter(id => !freeIds.contains(id + 1) && !freeIds.contains(id - 1)).head
}.assert(_ == 519)

println(Suite.show(test.report()))
