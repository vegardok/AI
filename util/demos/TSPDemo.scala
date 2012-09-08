package it3708.util.demos

import it3708.util.TSPDataset
import it3708.ea.Simulator
import it3708.ea.problem.{TSP_ERO, TSP_Bin, TSP_PMX}

/**
 * User: vegardok
 * Date: Feb 14, 2010
 */

object TSPDemo {
  def main(args: Array[String]) {
    if (args.length == 0) {
      val rounds = 10
      val datasets = Map(
        //(("wi29.tsp" -> 27603),
        ("dj38.tsp" -> 6656))
      for (t <- datasets) {
        for (i <- 0 until rounds) {
          println(i + " TSP, BIN " + t._1)
          new Simulator(new TSP_Bin(new TSPDataset(t._1, t._2), t._2))
        }
        for (i <- 0 until rounds) {
          println(i + " TSP, PMX " + t._1)
          new Simulator(new TSP_PMX(new TSPDataset(t._1, t._2), t._2))
        }
        for (i <- 0 until rounds) {
          println(i + " TSP, ERO " + t._1)
          new Simulator(new TSP_ERO(new TSPDataset(t._1, t._2), t._2))
        }
      }
    }else if(args.length == 3){
      args(0) match {
        case "pmx" =>{
          new Simulator(new TSP_PMX(new TSPDataset(args(1), Integer.parseInt(args(2))), Integer.parseInt(args(2))))
        }
        case "ero" =>{
          new Simulator(new TSP_ERO(new TSPDataset(args(1), Integer.parseInt(args(2))), Integer.parseInt(args(2))))
        }
        case "bin" =>{
          new Simulator(new TSP_Bin(new TSPDataset(args(1), Integer.parseInt(args(2))), Integer.parseInt(args(2))))
        }
      }
    }
    else{
      println("Usage: java -cp .. it3708.utils.demos.TSPDemo method (\"pmx\", \"ero\", \"bin\") .tsp-file optimal-length")
    }
  }
}