// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import $ivy.`co.fs2::fs2-io:2.4.6`
import fs2.{io, text, Chunk, Pipe, Stream}
import cats.effect.{Blocker, ExitCode, IO, IOApp}

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import java.nio.file.Paths

object Memory extends IOApp {
  val initial = read.lines ! pwd / "day15.txt" map (_.split(",").map(_.toInt)) head

  case class State(seen: Map[Int, Int], next: Int, size: Int)
  object State {
    def apply(initial: Array[Int]): State = State(Map(initial.init.zipWithIndex: _*), initial.last, initial.init.size)
  }

  val game = Stream(initial.init.toSeq: _*) ++ Stream.unfold(State(initial)) { state =>
    val next = state.seen.get(state.next) match {
      case None       => 0
      case Some(seen) => state.size - seen
    }
    Some(state.next, State(state.seen.updated(state.next, state.size), next, state.size + 1))
  }

  def checkElement(num: Int)(el: Int) =
    test(s"check ${num}th element") {
      game.drop(num - 1).take(1).compile.toList.head
    }.check(_ == el)

  def run(args: List[String]): IO[ExitCode] =
    IO.unit *>
      IO(checkElement(2020)(639)) *>
      IO(checkElement(30000000)(266)) *>
      IO(println(Suite.show(test.report()))) *>
      IO(ExitCode.Success)
}

Memory.main(Array())
