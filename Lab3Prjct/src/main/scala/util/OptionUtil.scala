package util

object OptionUtil {

  def option[A](body : => A) : Option[A] = {
    try
      Some(body)
    catch
      case e : Exception => None
  }

  def unit[A](body : => A) : Option[A] = {
    try
      Some(body)
    catch
      case e : Exception => None
  }

}
