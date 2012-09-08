package it3708.ann

import it3708.util.TSPDataset
import java.io.{File, PrintStream}

/**
 * User: vegardok
 * Date: Mar 5, 2010
 */

class SOM(dataset: TSPDataset) {
  var n_neurons: Int = {dataset.dimensions * 1.1}.toInt
  val scaled = dataset.scaled
  var start_neighbours: Int = {n_neurons * 0.5}.toInt
  val step = 1.0 / 100
  var neurons: Map[Int, Tuple2[Double, Double]] = Map[Int, Tuple2[Double, Double]]()
  for (i <- 0 until n_neurons) {
    neurons += (i -> (Math.random, Math.random))
  }
  neurons = neuronCircle(n_neurons, 0.5, 0.5, 0.5)

  def neuronCircle(n: Int, radius: Double, x: Double, y: Double) = {
    import Math._
    val deg = 360.0 / n
    var neurons = Map[Int, Tuple2[Double, Double]]()
    val deg2rad = Math.Pi / 180

    for{i <- 0 to n
        rad = i * deg * deg2rad} {
      neurons += (i -> (x + cos(rad) * radius, y + sin(rad) * radius))
    }
    neurons
  }


  def closestNeuron(city: Tuple2[Double, Double], neurons: Map[Int, Tuple2[Double, Double]]) = {
    def length(a: Tuple2[Double, Double], b: Tuple2[Double, Double]) = {
      Math.sqrt(Math.pow(Math.abs(a._1 - b._1), 2) + Math.pow(Math.abs(a._2 - b._2), 2))
    }

    var best_id = 0
    var best: Double = length(city, neurons(0))

    for{neuron <- neurons
        l = length(city, neuron._2)} {
      if (l < best) {
        best = l
        best_id = neuron._1

      }
    }
    best_id
  }

  //def tspPath(cities:Map[Int, List[Int]], neurons:Map[Int, Tuple2[Double, Double]]):List[Int] = {
  def tspPath(): List[Int] = {
    val cities = scaled.cities
    val mapping: List[Tuple2[Int, Int]] = {
      for (city <- cities) yield
        (closestNeuron(new Tuple2(city._2._1, city._2._2), neurons) -> city._1)
    }.toList
    mapping.sort((t1, t2) => t1._1 <= t2._1).map(_._2)
  }

  var bestD = 0.0;
  def log(path: String, iter: Int) = {
    val file = new PrintStream(new File(path))
    val neuron_copy = neurons.toList.sort((a, b) => a._1 <= b._1)
    var cities = scaled.cities.toList
    val label = dataset.optimal / dataset.length(tspPath())
    if (label > bestD)
      bestD = label
        file.print("\"foo\" \t \"%s-TSP fitness:%1.3f(%1.3f) @ iter %d\" \t \"foo\" \t \"Cities\"  \n".format(dataset.path, label, bestD, iter))
        for{neuron <- neuron_copy} {
          file.print(neuron._2._1.toString + "\t" + neuron._2._2.toString + "\t")
          if (cities.length > 0) {
            val city = cities.first
            cities = cities - city
            file.print(city._2._1.toString + "\t" + city._2._2.toString)
          }
          file.print("\n")
        }
        file.print(neuron_copy.first._2._1.toString + "\t" + neuron_copy.first._2._2.toString)
        file.close
    //    println(("echo  \"set terminal png size 600, 600;" +
    //            "set key autotitle columnhead;" +
    //            "set output \\\"" + path.replace(".dat", ".png") + "\\\";" +
    //            "plot \\\"%s\\\" u 1:2 with lines, \\\"%s\\\" u 3:4 with points\"|gnuplot;").format(path, path))
  }


  def run(iterations: Int) = {
    //    for (N <- List[Double](0.5, 0.9, 1.0, 1.1, 1.5, 2, 5))
    //      for (NH <- List[Double](0.01, 0.1, 0.2, 0.5))
    //        for (A_Max <- List[Double](1.0, 0.9, 0.5))
    //          for (A_Min <- List[Double](0.1, 0.05, 0.01)) {
   


    n_neurons = {dataset.dimensions * 0.9}.toInt
    start_neighbours = {n_neurons * 0.2}.toInt
    val A_Max = 0.9
    val A_Min = 0.1

    neurons = Map[Int, Tuple2[Double, Double]]()
    for (i <- 0 until n_neurons) {
      neurons += (i -> (Math.random, Math.random))
    }
    def α(iter: Int) = {
      //Math.max(1 - ( (1 / iterations) * iter), 0.1)
      val foo = A_Max - ((1.0 / iterations) * iter)
      Math.max(foo, A_Min)
    }
    def Θ(iter: Int, winner: Int) = {
      val n_neighbours_pr_side = Math.max(Math.floor((start_neighbours.toFloat / iterations ) * (iterations - iter)).toInt, 0)
      var neighbours = List[Tuple2[Int, Int]]()
      for (i <- 1 until {1 + n_neighbours_pr_side}) {
        neighbours = (i, (winner + i) % n_neurons) :: neighbours
        if (winner - i >= 0)
          neighbours = (i, winner - i % n_neurons) :: neighbours
        else
          neighbours = (i, (n_neurons - 1) - i % n_neurons) :: neighbours
      }
      // println(neighbours.sort((a,b)=>(a._1 <= b._1)))
      neighbours
    }
    bestD = 0
    for{i <- 0 to iterations} {
      if (i == 0 || i == iterations)
        for (foo <- 0 to 30)
          log("logs/" + dataset.path.replace(".", "-") + "%03d-%d-dat".format(i, foo), i)
      else
        log("logs/" + dataset.path.replace(".", "-") + "%03d-dat".format(i), i)
      for{city <- scaled.cities
          winner = closestNeuron(city._2, neurons)} {

        neurons = neurons.update(winner, Tuple2(
          neurons(winner)._1 + ((city._2._1 - neurons(winner)._1) * α(i)),
          neurons(winner)._2 + ((city._2._2 - neurons(winner)._2) * α(i))))
        for (n <- Θ(i, winner)) {
          neurons = neurons.update(n._2, Tuple2(
            neurons(n._2)._1 + ((α(i) / n._1) * (city._2._1 - neurons(n._2)._1)),
            neurons(n._2)._2 + ((α(i) / n._1) * (city._2._2 - neurons(n._2)._2))))
        }
      }
    }
    //            println("%1.4f(b), %1.2f(l) - N: %1.1f, NH: %1.1f, A_Max: %1.2f, A_Min: %1.2f".format(
    //                  bestD, dataset.optimal / dataset.length(tspPath), N, NH, A_Max, A_Min))
    println(" Final path: %1.4f (%1.4f)".format(dataset.optimal / dataset.length(tspPath), bestD))
  }
  //       }
}