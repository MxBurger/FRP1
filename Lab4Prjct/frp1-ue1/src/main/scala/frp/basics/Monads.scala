package frp.basics

object Monads:

  private def traditionalErrorHandling(): Unit = {}

  private def monadCallbacks(): Unit = {}

  private def monadCombinators(): Unit = {}

  def main(args: Array[String]): Unit =
    println("======== traditionalErrorHandling ===========")
    traditionalErrorHandling()
    println()

    println("============= monadCallbacks ================")
    monadCallbacks()
    println()

    println("============ monadCombinators ===============")
    monadCombinators()
    println()
  end main

end Monads