// scala 2.13.3

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import $ivy.`co.fs2::fs2-io:2.4.6`
import fs2.{io, text, Chunk, Pipe, Stream}
import cats.Show
import cats.effect.{Blocker, ExitCode, IO, IOApp}

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

import java.nio.file.Paths

sealed trait Input extends Product with Serializable
implicit val showInput: Show[Input] = Show.fromToString

case class Mask(mask: String) extends Input
case class Command(addr: Int, value: Int) extends Input

implicit val showMask: Show[Mask] = Show.fromToString
implicit val showCommand: Show[Command] = Show.fromToString

object Computer extends IOApp {
  val commands = Stream.resource(Blocker[IO]).flatMap { blocker =>
    io.file
      .readAll[IO](Paths.get("day14.txt"), blocker, 4096)
      .through(text.utf8Decode)
      .through(text.lines)
      .dropLast
      .map {
        case s"mask = $mask"        => Mask(mask)
        case s"mem[$addr] = $value" => Command(addr.toInt, value.toInt)
      }
  }

  def to36bitString(i: Int) =
    f"${i.toBinaryString}%36s".replace(' ', '0')

  def checkSum(sum: BigInt)(memory: Map[BigInt, BigInt]) =
    test("sum of all the values in memory") {
      memory.values.sum
    }.check(_ == sum)

  val program = commands
    .zipWithScan(Option.empty[Mask]) {
      case (mask, input) =>
        input match {
          case m: Mask => Some(m)
          case _       => mask
        }
    }
    .collect {
      case (c: Command, Some(m)) => (c, m)
    }

  def decoderV1[F[_]]: Pipe[F, (Command, Mask), Map[BigInt, BigInt]] =
    _.fold(Map.empty[BigInt, BigInt]) {
      case (memory, (command, mask)) =>
        val modified = to36bitString(command.value).zip(mask.mask).map {
          case (value, 'X')  => value
          case (value, mask) => mask
        }
        memory.updated(command.addr, BigInt(modified.mkString, 2))
    }

  def decoderV2[F[_]]: Pipe[F, (Command, Mask), Map[BigInt, BigInt]] =
    _.fold(Map.empty[BigInt, BigInt]) {
      case (memory, (command, mask)) =>
        val modified = to36bitString(command.addr).zip(mask.mask).map {
          case (value, '0')  => value
          case (value, mask) => mask
        }

        def expand(addresses: Seq[String]): Seq[String] =
          addresses.partition(_.contains('X')) match {
            case (expandable, rest) if expandable.nonEmpty =>
              val address = expandable.head
              val idx = address.indexOf('X')
              expand(address.updated(idx, '0') +: address.updated(idx, '1') +: expandable.tail ++: rest)
            case (_, rest) => rest
          }

        expand(Seq(modified.mkString)).foldLeft(memory) {
          case (memory, address) => memory.updated(BigInt(address.mkString, 2), command.value)
        }
    }

  def run(args: List[String]): IO[ExitCode] =
    program.through(decoderV1).map(checkSum(BigInt("14553106347726"))).showLinesStdOut.compile.toList *> program
          .through(decoderV2)
          .map(checkSum(BigInt("2737766154126")))
          .showLinesStdOut
          .compile
          .toList *> IO(
          println(Suite.show(test.report()))
        ) *> IO(ExitCode.Success)
}

Computer.main(Array())
