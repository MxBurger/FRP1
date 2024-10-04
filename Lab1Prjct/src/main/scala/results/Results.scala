package results

import java.nio.file.{Files, Paths}
import scala.io.Source
import scala.language.postfixOps

case class Results(id: Int, name: String, points: IndexedSeq[Int])

enum Grade :
  case EXCELLENT, GOOD, SATISFACTORY, SUFFICIENT, INSUFFICIENT

object ResultsAnalysis {

  def main(args: Array[String]): Unit = {
    val lines = Source.fromFile("files/results.csv").getLines.toList

    // Task 4.1: List of Results objects

    val resultList : List[Results] = ???
    println(resultList)

    // Task 4.2: Number of solved tasks

    val nSolvedPerStnd  = ???
    println(nSolvedPerStnd )


    // Task 4.3: Sufficient tasks solved

    val sufficientSolved = ???
    println(sufficientSolved)

    // Task 4.4: Grading

    val grades = ???
    println(grades)

    // Task 4.5: Grade statistics

    val nStudentsWithGrade = ???
    println(nStudentsWithGrade)

    // Task 4.6: Number solved per assignment

    val nSolvedPerAssnmt = ???
    println(nSolvedPerAssnmt)

    // Task 4.7.: Average points per assignment

    val avrgPointsPerAssnmt = ???
    println(avrgPointsPerAssnmt)

  }

  private def computeGrade(points: IndexedSeq[Int]): Grade = {
    if (points.count(p => p >= 3) < 8) then Grade.INSUFFICIENT
    else {
      val avrg = points.sorted.reverse.drop(2).sum / 8
      if (avrg < 5.0) then Grade.INSUFFICIENT
      else if (avrg < 6.5) then Grade.SUFFICIENT
      else if (avrg < 9.0) then Grade.SATISFACTORY
      else if (avrg < 8.0) then Grade.GOOD
      else Grade.EXCELLENT
    }
  }

}
