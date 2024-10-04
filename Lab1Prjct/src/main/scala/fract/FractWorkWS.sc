import fract._ // import all members of the fract package (similar to Java's import fract.*)

val f1 = Fract(1, 2)
println(f1)

val f2 = Fract(1, 3)

f1 == f2

f1 + f2

val f3 = Fract(3)

Fract(1,2) + (2 * Fract(1, 3))

1.+(2) // 1 + 2

1\2 + 2\3 * 2

if(f1 > f2) println("f1 greater f2") else println("f2 greater f1")

printSomething