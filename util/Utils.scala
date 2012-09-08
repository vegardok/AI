package it3708.util

import it3708.ea._

/**
 * User: vegardok
 * Date: Jan 31, 2010
 */


object Utils {
//  def standardDeviation(fitness: (Individual) => Double)(pop: Population): Double = {
//    val mean: Double = pop.population.map[Double](i => fitness(i)).reduceRight[Double](_ + _) / pop.size
//    Math.sqrt(
//      pop.population.map(ind => {
//        Math.pow(ind.fitness(fitness) - mean, 2)
//      }).reduceRight[Double]((a, b) => a + b) / pop.size
//      )
//  }

  val rand = new Random()

  def randInt(upto: Int, excluding: Int) = {
    var foo = rand.nextInt(upto)
    if (foo == excluding)
      foo = (foo + 1 % upto)
    foo
  }


  val codes = Map(
    (List(false, false, false, false) -> 0),
    (List(false, false, false, true) -> 1),
    (List(false, false, true, true) -> 2),
    (List(false, false, true, false) -> 3),
    (List(false, true, true, false) -> 4),
    (List(false, true, true, true) -> 5),
    (List(false, true, false, true) -> 6),
    (List(false, true, false, false) -> 7),
    (List(true, true, false, false) -> 8),
    (List(true, true, false, true) -> 9),
    (List(true, true, true, true) -> 10),
    (List(true, true, true, false) -> 11),
    (List(true, false, true, false) -> 12),
    (List(true, false, true, true) -> 13),
    (List(true, false, false, true) -> 14),
    (List(true, false, false, false) -> 15))
  def greyCodeToInt(greyCode: List[Boolean]) = {
    try{
      codes(greyCode)
    }catch{
      case ne:NoSuchElementException => throw new Exception("Unsuportet grey code: " + greyCode)
      case e => throw e
    }
  }

  var cache = Map[List[Boolean], Int]()
  def binToInt(bin:List[Boolean]) = {
    if(cache.contains(bin))
      cache(bin)
    else{
      var sum = 0.0
      for(i <- 0 until bin.length)
        if(bin((bin.length -1 )- i ))
          sum +=  Math.pow(2, i)
      cache = cache + (bin -> sum.toInt)
      cache(bin)
    }
  }
}

