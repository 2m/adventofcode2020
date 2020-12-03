import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

def lineToArray(line: String) =
  line.map {
    case '.' => false
    case '#' => true
  }.toArray

val map = read.lines ! pwd / "day3.txt" map lineToArray toArray

val height = map.length
val width = map(0).length

def slope(right: Int, down: Int) =
  for {
    y <- (0 until height by down)
    x = (y / down * right) % width
    if map(y)(x)
  } yield ()

def trees(right: Int, down: Int, count: Int) =
  test(s"tree count right", right = right, down = down, count = count) {
    slope(right, down).size
  }.check(_ == count)

test("tree product") {
  Seq(trees(1, 1, 70), trees(3, 1, 171), trees(5, 1, 48), trees(7, 1, 60), trees(1, 2, 35))
}.assert(_.fold(1)(_ * _) == 1206576000)

println(Suite.show(test.report()))
