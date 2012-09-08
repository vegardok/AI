package it3708.util.demos

/**
 * User: vegardok
 * Date: Feb 7, 2010
 */

object OneMaxDemo {
  import it3708.ea._
  import it3708.ea.problem._
  def main(args:Array[String]) {
    new Simulator(
      new OneMax()
      )
  }
}