This repository contains solutions to various puzzles of the [Advent of Code 2020][aoc2020].
Puzzles are solved with various idioms and libraries from the Scala ecosystem.

[Ammonite][amm] is used to run the code.
It is available from many package managers (e.g. `brew install ammonite-repl`).
When running a puzzle solution (e.g. `amm day2.sc`) dependencies will be downloaded automatically.
[Probably][probably] will register any assertions in the code and will print a summary:

```
┌─────┬────────┬──────────────────────────────────────────────┬───────┬───────┐
│     │ Hash   │ Test                                         │ Time  │ Debug │
├─────┼────────┼──────────────────────────────────────────────┼───────┼───────┤
│  ✓  │ 71d872 │ valid paswords by the first validation rule  │ 0.046 │       │
│  ✓  │ baa61a │ valid paswords by the second validation rule │ 0.001 │       │
└─────┴────────┴──────────────────────────────────────────────┴───────┴───────┘
Passed: 2   Failed: 0   Total: 2

 ✓  Pass                               ✗  Fail
 ?  Throws in check                    !  Throws in body
 ±  Fails sometimes                    #  Suite partially fails
```

List of solutions and Scala idioms/libraries used:

| Puzzle             | Idioms and libraries                                                   |
| ------------------ | ---------------------------------------------------------------------- |
| [Day 1](day1.sc)   | [Ammonite Ops][amm-ops], set operations                                |
| [Day 2](day2.sc)   | [Regex pattern matching][regex]                                        |
| [Day 3](day3.sc)   | [For Comprehensions][for]                                              |
| [Day 4](day4.sc)   | [Akka Streams][akka-streams], [Match on interpolator][match-interp]    |
| [Day 5](day5.sc)   | [Parsing text to integers][parse-int]                                  |
| [Day 6](day6.sc)   | [fs2][], [semigroup][]                                                 |
| [Day 7](day7.sc)   | [scala-graph][]                                                        |
| [Day 8](day8.sc)   | [var][], [Mutable Collections][mutable-coll]                           |
| [Day 9](day9.sc)   | [Collections Sliding Window, Tails][subsets]                           |
| [Day 10](day10.sc) | [Collections Sliding Window][subsets]                                  |
| [Day 11](day11.sc) | [Try, Success, Failure][try]                                           |
| [Day 12](day12.sc) | [fs2][], [LazyList][lazy-list]                                         |
| [Day 13](day13.sc) | [minBy][]                                                              |
| [Day 14](day14.sc) | [fs2][]                                                                |
| [Day 15](day15.sc) | [fs2][]                                                                |
| [Day 16](day16.sc) |                                                                        |

[aoc2020]:      https://adventofcode.com/2020
[amm]:          https://ammonite.io/
[amm-ops]:      https://ammonite.io/#Operations
[probably]:     https://github.com/propensive/probably
[regex]:        https://www.scala-lang.org/api/2.13.3/scala/util/matching/Regex.html
[for]:          https://docs.scala-lang.org/tour/for-comprehensions.html
[akka-streams]: https://doc.akka.io/docs/akka/current/stream/stream-quickstart.html
[match-interp]: https://cucumbersome.net/2020/11/28/four-new-features-of-scala-2-13-releases-that-you-probably-missed/#2130-s-interpolator-on-pattern-matching
[parse-int]:    https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Integer.html#parseInt(java.lang.String,int)
[fs2]:          https://fs2.io/guide.html
[semigroup]:    https://typelevel.org/cats/typeclasses/semigroup.html
[scala-graph]:  https://www.scala-graph.org/guides/core-initializing.html
[var]:          https://docs.scala-lang.org/overviews/scala-book/two-types-variables.html
[mutable-coll]: https://docs.scala-lang.org/overviews/collections-2.13/concrete-mutable-collection-classes.html
[subsets]:      https://alvinalexander.com/scala/how-to-split-sequences-subsets-groupby-partition-scala-cookbook/
[try]:          https://docs.scala-lang.org/overviews/scala-book/functional-error-handling.html#trysuccessfailure
[lazy-list]:    https://www.scala-lang.org/api/current/scala/collection/immutable/LazyList.html
[minBy]:        https://allaboutscala.com/tutorials/chapter-8-beginner-tutorial-using-scala-collection-functions/scala-minby-example/
