// scala 2.13.3

import $ivy.`com.propensive::probably-cli:0.5.0`
import probably._, global._

import ammonite.ops._

case class Password(lower: Int, upper: Int, char: Char, password: String) {
  def valid = {
    val occurences = password.groupBy(identity).mapValues(_.size).getOrElse(char, 0)
    lower to upper contains occurences
  }

  def validV2 = password.charAt(lower - 1) == char ^ password.charAt(upper - 1) == char
}

object Password {
  val PasswordRegex = """(\d+)-(\d+) (\w): (\w+)""".r
  def apply(str: String): Password =
    str match {
      case PasswordRegex(from, to, char, password) => Password(from.toInt, to.toInt, char.charAt(0), password)
    }
}

val passwords = read.lines ! pwd / "day2.txt" map Password.apply

test("valid paswords by the first validation rule") {
  passwords.filter(_.valid).size
}.assert(_ == 564)

test("valid paswords by the second validation rule") {
  passwords.filter(_.validV2).size
}.assert(_ == 325)

println(Suite.show(test.report()))
