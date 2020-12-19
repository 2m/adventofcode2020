// scala 2.13.3

import ammonite.ops._

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import $ivy.`co.fs2::fs2-io:2.4.6`
import fs2.{io, text, Chunk, Stream}
import cats.effect.{Blocker, ExitCode, IO, IOApp}

import java.nio.file.Paths

object Navigation extends IOApp {
  trait Direction
  case object North extends Direction
  case object East extends Direction
  case object South extends Direction
  case object West extends Direction

  val directions: LazyList[Direction] = North #:: East #:: South #:: West #:: directions
  def rotate(d: Direction, right: Int) =
    directions.dropWhile(_ != d).drop(Math.floorMod(right, 4)).head

  def rotateWp(s: State, right: Int): State =
    right match {
      case 0 => s
      case t => rotateWp(s.copy(wx = s.wy * -1, wy = s.wx), right - 1)
    }

  case class State(x: Int = 0, y: Int = 0, wx: Int = 0, wy: Int = 0, d: Direction = East) {
    def applyInstruction(ins: String) =
      ins match {
        case s"N$i" => this.copy(y = this.y - i.toInt)
        case s"S$i" => this.copy(y = this.y + i.toInt)
        case s"E$i" => this.copy(x = this.x + i.toInt)
        case s"W$i" => this.copy(x = this.x - i.toInt)
        case s"R$i" => this.copy(d = rotate(this.d, i.toInt / 90))
        case s"L$i" => this.copy(d = rotate(this.d, -i.toInt / 90))
        case s"F$i" =>
          val (xd, yd) = d match {
            case North => (0, -1)
            case East  => (1, 0)
            case South => (0, 1)
            case West  => (-1, 0)
          }
          this.copy(x = this.x + (i.toInt * xd), y = this.y + (i.toInt * yd))
        case _ => this
      }

    def applyWaypoint(ins: String) = {
      val s = ins match {
        case s"N$i" => this.copy(wy = this.wy - i.toInt)
        case s"S$i" => this.copy(wy = this.wy + i.toInt)
        case s"E$i" => this.copy(wx = this.wx + i.toInt)
        case s"W$i" => this.copy(wx = this.wx - i.toInt)
        case s"R$i" => rotateWp(this, i.toInt / 90)
        case s"L$i" => rotateWp(this, Math.floorMod(-i.toInt / 90, 4))
        case s"F$i" =>
          this.copy(x = this.x + (i.toInt * this.wx), y = this.y + (i.toInt * this.wy))
        case _ => this
      }
      println(s)
      s
    }
  }

  val instructions = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](Paths.get("day12.txt"), blocker, 4096)
      .through(text.utf8Decode)
      .through(text.lines)
  }

  val shipDestination = instructions.fold(State())(_ applyInstruction _).head
  val waypointDestination = instructions.fold(State(0, 0, 10, -1))(_ applyWaypoint _).head

  def checkShipdestination(s: State) =
    test("ship destination manhattan distance", state = s.toString) {
      s.x.abs + s.y.abs
    }.assert(_ == 2297)

  def checkWaypointdestination(s: State) =
    test("waypoint destination manhattan distance", state = s.toString) {
      s.x.abs + s.y.abs
    }.assert(_ == 89984)

  def run(args: List[String]): IO[ExitCode] =
    shipDestination.compile.toList.map(_.head).map(checkShipdestination) *>
      waypointDestination.compile.toList.map(_.head).map(checkWaypointdestination) *>
      IO(ExitCode.Success)
}

Navigation.main(Array())
println(Suite.show(test.report()))
