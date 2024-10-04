package graph

object SimpleGraph extends IntGraph with App {
  val nodes: Set[N] = Set(1, 2, 3)
  val edges: Set[(N, N)] = Set((1, 2), (1, 3), (2, 4), (3, 4))

  val dists = computeDists(1)
  println(dists)

  val paths = computePaths(1)
  println(paths)

  val distsG = computeDistsG(1)
  println(distsG)

  val pathsG = computePathsG(1)
  println(pathsG)

}

