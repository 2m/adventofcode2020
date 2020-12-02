// scala 2.13.3

import ammonite.ops._

case class Password(from: Int, to: Int, char: Char, password: String) {
  def valid = {
    val occurences = password.groupBy(identity).mapValues(_.size).getOrElse(char, 0)
    from to to contains occurences
  }

  def validV2 = password.charAt(from - 1) == char ^ password.charAt(to - 1) == char
}

object Password {
  val PasswordRegex = """(\d+)-(\d+) (\w): (\w+)""".r
  def apply(str: String): Password =
    str match {
      case PasswordRegex(from, to, char, password) => Password(from.toInt, to.toInt, char.charAt(0), password)
    }
}

val passwords = read.lines ! pwd / "day2.txt" map Password.apply

println(passwords.filter(_.valid).size) // 564
println(passwords.filter(_.validV2).size) // 325
