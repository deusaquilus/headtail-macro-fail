# headtail-macro-fail
Example of a Scala 3 macro HeadTail causing a bogus binding failure

This was originally an attempt to make a macro that will actually deconstruct a tuple into a properly typed head and tail as h and t respectively 
so you don’t need to use erasedValue. The alternative to this approach was something like this:
(attempting to write a inline-function that will concat tuple elements into a string)
```scala
import scala.compiletime.erasedValue

inline def concat[T <: Tuple](inline args: T): String =
  inline erasedValue[T] match
    case EmptyTuple => ""
    case _: (h *: t) =>
      args.productElement(0).toString + concat(args.drop(1))

val v = concat("Hello" *: 42 *: EmptyTuple)
println(v)
```

It was found that you have to add a bogus binding variable i.e. bogusBinding in order for it to work (same problem in 3.1.3 and 3.2.0). 
This is an attached repo for the dotty issue: https://github.com/lampepfl/dotty/issues/15971

The code is meant to be used like this:
```scala
object HeadTailUse {
  inline def concat[T <: Tuple](inline args: T): String =
    inline MatchHeadTail(args) match
      case HeadTail.Nil =>
        ""
      // head will actually have type `h` and tail will have type `t`
      // but you need to keep `bogusBinding` 
      case bogusBinding @ HeadTail.Pair(head, tail): HeadTail.Pair[h, t] =>
        head.toString + concat(tail)

  def main(args: Array[String]) = {
    println(concat("Hello" *: 42 *: EmptyTuple))
  }
}
```
