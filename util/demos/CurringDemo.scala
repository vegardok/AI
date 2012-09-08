package it3708.util

/**
 * User: vegardok
 * Date: Feb 4, 2010
 */

//object BlottoDemo {
//  import it3708.ea._
//  import it3708.ea.problem._
//  def main(args:Array[String]) {
//    var loggers = List[ResultLogger]()
//    val battles = List[Int](5, 10, 20)
//    val Rs = List[Double](0.0, 0.5, 1.0)
//    val Ls = List[Double](0.0, 1.0/50, 1.0/25)
//
//    for{Bconf <- battles
//        Rconf <- Rs
//        Lconf <- Ls} {
//      val title = "colonel-blotto_B:%d_R:%1.2f_L:%1.2f".format(Bconf, Rconf, Lconf).replace(".", "")
//      val sim = new Simulator(
//        new ColonelBlotto(title +  ".log.dat"){
//          println("New run: " + title.replace("_", " ") )
//
//          override def battles = Bconf
//          override def R = Rconf
//          override def L = Lconf
//
//        }
//  )
//  loggers = sim.logger :: loggers
//}
//
//new ResultComparer("blotto", loggers)
//  }
//}