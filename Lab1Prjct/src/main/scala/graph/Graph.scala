package graph

import scala.collection.immutable.Queue

trait Graph {
  type N  // abstract type for nodes

  val nodes: Set[N]
  val edges: Set[(N, N)]

  def successors(node: N) : Set[N] = ???

  def computeDists(start: N): Map[N, Int] = ??? 

  def computePaths(start: N): Map[N, List[N]] = ??? 

  def computeValues[R](start: N, startValue: R, resultFn: (N, R) => R): Map[N, R] = ???

  def computeDistsG(start: N): Map[N, Int] = ???


  def computePathsG(start: N): Map[N, List[N]] = ???

}

trait IntGraph extends Graph {
  type N = Int
}


