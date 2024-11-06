package results

import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.language.postfixOps

case class Results(id: Int, name: String, points: IndexedSeq[Int])

enum Grade :
  case EXCELLENT, GOOD, SATISFACTORY, SUFFICIENT, INSUFFICIENT

object ResultsAnalysis {

  def main(args: Array[String]): Unit = {

    val lines: List[String] = Source.fromFile("files/results.csv").getLines.toList

    // Task 4.1: List of Results objects

    val resultList: List[Results] =
      lines.drop(1)
        .map(l => l.split(","))
        .map(arr => arr.map(e => e.trim)) // remove whitespaces
        .filter(arr => arr.length == 12)
        .map(arr => Results(arr(0).toInt, arr(1),
          arr.drop(2).map(p => p.toInt).toVector // no array because they are mutable
        ))
    println(resultList)

    // Task 4.2: Number of solved tasks

    val nSolvedPerStnd: Map[String, Int] =
      resultList
        .map(r => (r.name, r.points.count(p => p >= 3))).toMap

    println(nSolvedPerStnd)


    // Task 4.3: Sufficient tasks solved

    val sufficientSolved: (Set[String], Set[String]) = {
      val (pos, neg)  = nSolvedPerStnd.partition((name, n) => n >= 8)
      (pos.keySet, neg.keySet)
    }
    println(sufficientSolved)



    // Task 4.4: Grading

    val grades : Map[String, Grade] = {
      resultList.map(r => (r.name, computeGrade(r.points))).toMap
    }
    println(grades)



    // Task 4.5: Grade statistics

    val nStudentsWithGrade : Map[Grade, Int] = {
      val grades = resultList.map(r => computeGrade(r.points))
      grades.groupBy(g => g).map((g, gs) => (g, gs.size))
    }
    println(nStudentsWithGrade)



    // Task 4.6: Number solved per assignment

    val nSolvedPerAssnmt : List[(Int, Int)] = {
      (1 to 10).map(i => {
        val nSolved = resultList.map(r => r.points(i - 1)).count(p => p >= 3)
        (i, nSolved)
      }).toList
    }
    println(nSolvedPerAssnmt)


    // Task 4.7.: Average points per assignment

    val avrgPointsPerAssnmt : Map[Int, Double] =
      (1 to 10).map(i => {
        val valids = resultList.map(r => r.points(i - 1)).filter(p => p >= 0)
        val sum = valids.sum
        (i, sum.toDouble / valids.size)
      }).toMap
    println(avrgPointsPerAssnmt)
  }

  private def computeGrade(points: IndexedSeq[Int]): Grade = {
    if (points.count(p => p >= 3) < 8) then Grade.INSUFFICIENT
    else {
      val avrg = points.sorted.reverse.drop(2).sum / 8
      if (avrg < 5.0) then Grade.INSUFFICIENT
      else if (avrg < 6.5) then Grade.SUFFICIENT
      else if (avrg < 8.0) then Grade.SATISFACTORY
      else if (avrg < 9.0) then Grade.GOOD
      else Grade.EXCELLENT
    }
  }

}
