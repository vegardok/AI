package it3708.util.demos

import it3708.ann.SOM
import it3708.util.TSPDataset

/**
 * User: vegardok
 * Date: Mar 8, 2010
 */

object NeuronDemo {
  def main(args: Array[String]) {
    val i = 100
    val wi = new SOM(new TSPDataset("wi29.tsp", 27603))
    println("sahara:")
    wi.run(i)
    println()


    val dj = new SOM(new TSPDataset("dj38.tsp", 6656))
    println("dij")
    dj.run(i)
    println()

    val qa = new SOM(new TSPDataset("qa194.tsp", 9352))
    println("qutar:")
    qa.run(i)

//        val mu = new SOM(new TSPDataset("mu1979.tsp", 86891))
//    println("mu;")
//    mu.run(i)
  }
}