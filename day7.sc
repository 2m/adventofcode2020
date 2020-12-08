// scala 2.13.3

import ammonite.ops._

import $ivy.`org.scala-graph::graph-core:1.13.2`
import scalax.collection.Graph
import scalax.collection.GraphPredef._, scalax.collection.GraphEdge._
import scalax.collection.edge.WDiEdge

import $ivy.`com.propensive::probably-cli:0.8.0`
import probably._, global._

case class Bag(colour: String, capacity: List[Capacity])
case class Capacity(count: Int, colour: String)

def toRule(s: String) =
  s match {
    case s"$colour bags contain $bags" =>
      Bag(
        colour,
        bags
          .dropRight(1)
          .split(",")
          .toList
          .map(_.strip)
          .flatMap {
            case "no other bags"    => None
            case s"$count $to bag"  => Some(Capacity(count.toInt, to))
            case s"$count $to bags" => Some(Capacity(count.toInt, to))
          }
      )
  }

val rules = read.lines ! pwd / "day7.txt" map toRule

val toShinyGold = Graph.from(
  rules.map(_.colour),
  rules.flatMap(bag => bag.capacity.map(capacity => DiEdge(capacity.colour, bag.colour)))
)

test("colours containing shiny gold bag") {
  toShinyGold.get("shiny gold").outerNodeTraverser.size - 1
}.assert(_ == 101)

val fromShinyGold = Graph.from(
  rules.map(_.colour),
  rules.flatMap(bag => bag.capacity.map(capacity => WDiEdge(bag.colour, capacity.colour)(capacity.count)))
)

def bagsInside(prevCount: Int, bag: fromShinyGold.NodeT): List[Int] =
  bag.edges.filter(_.from == bag) match {
    case outgoing if outgoing.isEmpty => List(prevCount)
    case outgoing =>
      outgoing.toList.flatMap(edge => bagsInside(edge.weight.toInt * prevCount, edge.to)) ++ List(prevCount)
  }

test("shiny gold contains bags") {
  bagsInside(1, fromShinyGold.get("shiny gold")).sum - 1
}.assert(_ == 108636)

println(Suite.show(test.report()))
