package frp.basics.actors.klausur

case class Person(firstName: String, lastName: String, born: Int)


@main
def OrderingMain(): Unit =

  val orderByLastName: Ordering[Person] = Ordering.by(person => person.lastName)
  val orderByFirstName: Ordering[Person] = Ordering.by(person => person.firstName)
  val orderByBorn: Ordering[Person] = Ordering.by(person => person.born)


  val personOrdering =
    orderByLastName
      .orElse(orderByFirstName)
      .orElse(orderByBorn)
  val personOrderingRev: Ordering[Person] =
    Ordering.by[Person, String](person => person.lastName)
      .orElseBy(_.firstName)
      .orElseBy(_.born)
      .reverse
  val huberFranz1999 = Person("Franz", "Huber", 1990)
  val huberFranz1998 = Person("Franz", "Huber", 1991)
  println(personOrdering.lt(huberFranz1998, huberFranz1999))
  println(personOrderingRev.lt(huberFranz1998, huberFranz1999))




