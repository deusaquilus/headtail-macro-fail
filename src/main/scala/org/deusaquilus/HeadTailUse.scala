package org.deusaquilus

object HeadTailUse {

  inline def concat[T <: Tuple](inline args: T): String =
    inline MatchHeadTail(args) match
      case HeadTail.Nil =>
        ""
      // remove `bogusBinding @` and it will fail
      case bogusBinding @ HeadTail.Pair(head, tail): HeadTail.Pair[h, t] =>
        head.toString + concat(tail)

  def main(args: Array[String]) = {
    println(concat("Hello" *: 42 *: EmptyTuple))
  }
}
