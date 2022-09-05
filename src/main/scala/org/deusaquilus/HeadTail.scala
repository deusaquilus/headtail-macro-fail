package org.deusaquilus

import scala.quoted._

trait HeadTail
object HeadTail:
  case class Pair[H, T <: Tuple](h: H, t: T) extends HeadTail
  object Nil extends HeadTail

object MatchHeadTail {
  transparent inline def apply[A <: Tuple](inline tup: A): HeadTail = ${ applyImpl('tup) }
  def applyImpl[A <: Tuple](tup: Expr[A])(using q: Quotes, tpe: Type[A]): Expr[HeadTail] = {
    import quotes.reflect._
    tpe match
      case '[EmptyTuple] =>
        '{ HeadTail.Nil }
      case '[h *: t] =>
        '{ HeadTail.Pair[h, t]($tup.productElement(0).asInstanceOf[h], $tup.drop(1).asInstanceOf[t]) }
  }
}