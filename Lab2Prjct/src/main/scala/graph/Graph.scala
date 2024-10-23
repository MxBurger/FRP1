package graph

import scala.collection.immutable.Queue

trait Graph {
  type N  // abstract type for nodes

  val nodes: Set[N]
  val edges: Set[(N, N)]

  // Task 3.1: Method successors

  def successors(node: N) : Set[N] =
    edges.filter((f, t) => f == node ).map((f, t) => t)

  // Task 3.2: Method computeDists

  def computeDists(start: N): Map[N, Int] = ???

  // Task 3.3: Method computePaths

  def computePaths(start: N): Map[N, List[N]] = ???

  // Task 3.4: Methods compute Values

  // TODO Generic computeValues

  def computeDistsG(start: N): Map[N, Int] = ???

  def computePathsG(start: N): Map[N, List[N]] = ???

  // optimizations -------------------------------------------------------------------------------

}

trait IntGraph extends Graph {
  type N = Int
}


