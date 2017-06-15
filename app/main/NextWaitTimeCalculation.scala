package main

import scala.annotation.tailrec
import scala.util.control.TailCalls.TailRec

/**
  * Created by gunni on 15.06.2017.
  */
object NextWaitTimeCalculation {
  def main(args: Array[String]): Unit = {
    println(f(100,120,0))
    println(f(100,120,1))
    println(f(100,120,2))
    println(f(100,120,3))
  }

  def f(left: Int, right: Int, step: Int): Int = step match {
    case 0 => left.min(right)
    case Even(step) => left.min(right) - f(left, right,step-1)
    case step => left.max(right) - f(left,right,step-1)

  }

  //Ein "extractor"
  object Even {
    def unapply(x: Int) = if(x % 2 == 0) Some(x) else None
  }
}
