// scala 2.13.3

import scala.util.{Failure, Success, Try}

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val seats = read.lines ! pwd / "day11.txt"

val width = seats.head.length
val plan = seats.flatMap(_.toCharArray)

def numOfOccupied(id: Int, plan: Seq[Char]) =
  ((if (id  % width != 0) Seq(id - 1, id - width - 1, id + width - 1) else Seq.empty) ++
    (if (id % width != width - 1) Seq(id + 1, id - width + 1, id + width + 1) else Seq.empty) ++
    Seq(id + width, id - width))
    .map(plan.lift(_).getOrElse('.') match {
      case '#' => 1
      case _   => 0
    })
    .sum

def shuffleNear(plan: Seq[Char]) =
  plan.zipWithIndex.map {
    case ('L', idx) if numOfOccupied(idx, plan) == 0 => '#'
    case ('#', idx) if numOfOccupied(idx, plan) >= 4 => 'L'
    case (state, _)                                  => state
  }

def numSeen(id: Int, plan: Seq[Char]) = {
  val map = plan.grouped(width).toSeq
  val x = id % width
  val y = id / width
  val ray = seen(x, y, map) _
  ray(-1, -1) + ray(-1, 0) + ray(-1, 1) + ray(0, -1) + ray(0, 1) + ray(1, -1) + ray(1, 0) + ray(1, 1)
}

def seen(x: Int, y: Int, map: Seq[Seq[Char]])(xOff: Int, yOff: Int): Int =
  Try(map(y + yOff)(x + xOff)) match {
    case Success('.') => seen(x + xOff, y + yOff, map)(xOff, yOff)
    case Success('#') => 1
    case Success(_)   => 0
    case Failure(_)   => 0
  }

def shuffleSeen(plan: Seq[Char]) =
  plan.zipWithIndex.map {
    case ('L', idx) if numSeen(idx, plan) == 0 => '#'
    case ('#', idx) if numSeen(idx, plan) >= 5 => 'L'
    case (state, _)                            => state
  }

def stabilize(plan: Seq[Char], shuffle: Seq[Char] => Seq[Char]): Seq[Char] =
  shuffle(plan) match {
    case newPlan if newPlan == plan => plan
    case newPlan                    => stabilize(newPlan, shuffle)
  }

test("occupied in stabilized when shuffling near") {
  stabilize(plan, shuffleNear).count(_ == '#')
}.assert(_ == 2481)

test("occupied in stabilized when shuffling seen") {
  stabilize(plan, shuffleSeen).count(_ == '#')
}.assert(_ == 2227)

println()

println(Suite.show(test.report()))
