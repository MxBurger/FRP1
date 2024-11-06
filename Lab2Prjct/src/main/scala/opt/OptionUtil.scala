package opt

def option[A](code: => A): Option[A] = {
  try {
    Some(code)
  } catch
    case e: Exception => None
}