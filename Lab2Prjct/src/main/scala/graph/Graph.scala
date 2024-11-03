package graph

import scala.collection.immutable.Queue

trait Graph {
  type N  // abstract type for nodes

  val nodes: Set[N]
  val edges: Set[(N, N)]

  // Task 3.1: Method successors

  def successors(node: N) : Set[N] =
    edges.filter((f, t) => f == node ).map((_, t) => t)

  // Task 3.2: Method computeDists

  def computeDists(start: N): Map[N, Int] = {
    // breitensuche
    def distsRec(queue: Queue[N], result:Map[N, Int]): Map[N, Int] = {
      if (queue.isEmpty) result
      else {
        val node = queue.head
        val succs = successors(node).removedAll(queue).removedAll(result.keys)
        val updQueue = queue.tail.appendedAll(succs)
        val d = result(node) + 1
        val updResult = result ++ succs.map(s => (s, d))

        distsRec(updQueue, updResult)
      }
    }
    distsRec(Queue(start), Map(start -> 0))
  }


  // Task 3.3: Method computePaths

  def computePaths(start: N): Map[N, List[N]] = {
    def pathsRec(queue: Queue[N], result: Map[N, List[N]]): Map[N, List[N]] = {
      if (queue.isEmpty) {
        result
      }
      else {
        val node = queue.head
        val succs = successors(node).removedAll(queue).removedAll(result.keys)
        val updatedQueue = queue.tail.appendedAll(succs)
        val nodeResult = result(node)
        // :: appends
        val updatedResult = result ++ succs.map(s => (s, s :: nodeResult))
        pathsRec(updatedQueue, updatedResult)
      }
    }
    pathsRec(Queue(start), Map(start -> List(start)))
  }


  // Task 3.4: Methods compute Values

  def computeValues[R](start: N, startValue: R, fn: (N, R) => R) : Map[N, R] = {
    def valuesRec(queue: Queue[N], result: Map[N, R]): Map[N, R] = {
      if (queue.isEmpty) {
        result
      }
      else {
        val node = queue.head
        val succs = successors(node).removedAll(queue).removedAll(result.keys)
        val updatedQueue = queue.tail.appendedAll(succs)
        val nodeValue = result(node)
        val updatedResult = result ++ succs.map(s => (s, fn(s, nodeValue)))
        valuesRec(updatedQueue, updatedResult)
      }
    }
    valuesRec(Queue(start), Map(start -> startValue))
  }

  def computeDistsG(start: N): Map[N, Int] =
    computeValues(start, 0, (n, d) => d + 1)

  def computePathsG(start: N): Map[N, List[N]] =
    computeValues(start, List(start), (n, p) => n :: p)

  // optimizations -------------------------------------------------------------------------------

}

trait IntGraph extends Graph {
  type N = Int
}


