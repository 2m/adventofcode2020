// scala 2.13.3

import ammonite.ops._
import scala.collection.mutable

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

val badCode = read.lines ! pwd / "day8.txt"

def computer(code: Seq[String]): Either[Int, Int] = {
  val executed = mutable.Set.empty[Int]
  var ip = 0
  var acc = 0

  var continue = true
  while (continue)
    if (executed.contains(ip))
      continue = false
    else {
      executed += ip
      code.lift(ip) match {
        case Some(s"nop $a")      => ip += 1
        case Some(s"acc $value")  => acc += value.toInt; ip += 1
        case Some(s"jmp $offset") => ip += offset.toInt
        case None                 => return Right(acc)
      }
    }

  Left(acc)
}

test("acc value with bad code") {
  computer(badCode).left.get
}.assert(_ == 1915)

val results = badCode.zipWithIndex
  .collect {
    case (s"nop $arg", idx) => (idx, s"jmp $arg")
    case (s"jmp $arg", idx) => (idx, s"nop $arg")
  }
  .map((badCode.updated _).tupled)
  .map(computer)

test("acc value with fixed code") {
  results.collectFirst {
    case Right(value) => value
  }.get
}.assert(_ == 944)

println(Suite.show(test.report()))
