// scala 2.13.3

import ammonite.ops._

import $ivy.`com.typesafe.akka::akka-stream:2.6.10`
import akka.stream.scaladsl._
import akka.actor.ActorSystem

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import scala.concurrent.Await
import scala.concurrent.duration._

trait Entry
case object Break extends Entry
case class Property(v: String) extends Entry

val entries: Seq[Entry] = read.lines ! pwd / "day4.txt" flatMap {
      case ""   => Seq(Break)
      case line => line.split(" ").map(Property)
    }

object Main {
  def asInt(s: String) = s.toIntOption.getOrElse(0)

  def validProperties: PartialFunction[String, Unit] = {
    case s"byr:$year" if 1920 to 2002 contains asInt(year)                                       =>
    case s"iyr:$year" if 2010 to 2020 contains asInt(year)                                       =>
    case s"eyr:$year" if 2020 to 2030 contains asInt(year)                                       =>
    case s"hgt:${height}cm" if 150 to 193 contains asInt(height)                                 =>
    case s"hgt:${height}in" if 59 to 76 contains asInt(height)                                   =>
    case s"hcl:#$colour" if colour.forall(_.isLetterOrDigit) && colour.size == 6                 =>
    case s"ecl:$colour" if Seq("amb", "blu", "brn", "gry", "grn", "hzl", "oth").contains(colour) =>
    case s"pid:$id" if id.forall(_.isDigit) && id.size == 9                                      =>
  }

  def run() = {
    implicit val sys = ActorSystem()
    val res =
      Source(entries)
        .splitWhen(_ == Break)
        .collect {
          case Property(v) => v
        }
        .collect(validProperties)
        .fold(Seq.empty[Unit])(_ :+ _)
        .mergeSubstreams
        .collect {
          case entries if entries.size == 7 => entries
        }
        .runWith(Sink.seq)
    Await.result(res, 5.seconds)
  }
}

test("valid according to all rules") {
  Main.run()
}.assert(_.size == 179)

println(Suite.show(test.report()))
