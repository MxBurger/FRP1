package opt

def option[X](code: => X): Option[X] = {
  try {
    Some(code)
  } catch
    case e: Exception => None
}