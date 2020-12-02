// scala 2.13.3

import ammonite.ops._

val entries = read.lines ! pwd / "day1.txt" map (_.toInt) toSet

val tuple = entries.map(2020 - _).intersect(entries)
println(tuple) // HashSet(1938, 82)
assert(tuple.sum == 2020)

val triple = entries.map(2020 - _).map(e => entries.map(e - _)).flatMap(entries.intersect)
println(triple) // HashSet(372, 1307, 341)
assert(triple.sum == 2020)
