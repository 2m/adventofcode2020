// scala 2.13.3

import $ivy.`co.fs2::fs2-io:2.4.6`
import fs2.{io, text, Chunk, Stream}
import cats.effect.{Blocker, ExitCode, IO, IOApp}
import cats.Monoid

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import java.nio.file.Paths

object Converter extends IOApp {

  def union[A]: Monoid[Set[A]] =
    new Monoid[Set[A]] {
      def combine(x: Set[A], y: Set[A]) = x union y
      def empty: Set[A] = Set.empty
    }

  def intersect[A]: Monoid[Set[A]] =
    new Monoid[Set[A]] {
      def combine(x: Set[A], y: Set[A]) = x intersect y
      def empty: Set[A] = Set.empty
    }

  def foldHeadInit[A: Monoid](chunk: Chunk[A]) =
    chunk.head match {
      case Some(s) => chunk.foldLeft(s)(Monoid.combine)
      case None    => Monoid.empty
    }

  val allAnswers = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](Paths.get("day6.txt"), blocker, 4096)
      .through(text.utf8Decode)
      .through(text.lines)
      .split(_ == "")
      .map(_.map(_.toSet))
  }

  def answers(implicit m: Monoid[Set[Char]]) =
    allAnswers.map(foldHeadInit(_)(m)).map(_.size).foldMonoid.compile.toList.unsafeRunSync().head

  def run(args: List[String]): IO[ExitCode] = {
    test("anyone answered yes") {
      answers(union)
    }.assert(_ == 6662)

    test("all answered yes") {
      answers(intersect)
    }.assert(_ == 3382)

    IO(ExitCode.Success)
  }
}

Converter.main(Array())
println(Suite.show(test.report()))
