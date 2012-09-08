package it3708.ea

import it3708.util.Utils
import scala.actors.Futures._
/**
 * User: vegardok
 * Date: Feb 6, 2010
 */

class Population[IndType <: Individual](size: Int,
                 generation: Int,
                 individualSize: Int,
                 individuals: List[IndType]) {
  

  
  /**
   * Makes a new population of see(size) individuals
   * that each have a see(individualSize) bit long random
   * genome
   */

//  type Ind <: Individual
//  def this(size: Int, individualSize: Int) = {
//    this (size, 0, individualSize, (0 until size).map(id =>
//      {new Ind(individualSize)}).toList)
//  }

  def this(individuals: List[IndType], gen: Int) = {
    this (individuals.size, gen, individuals(0).size, individuals)
  }
  //new empty population
  def this(gen: Int) = {
    this (0, gen, 0, null)
  }
  
  val population = individuals
  val gen = generation

  def individual(id: Int) = {population(id)}

  def size(): Int = {
    if (population != null)
      population.length
    else
      0
  }
  
  override def toString() = {
    "Population: " + size + " ind size: " + individualSize +  " gen: " + gen
  }
  def verboseToString() = {
    var out = "Population: " + size + " ind size: " + individualSize +  " gen: " + gen
    for(ind <- individuals)
      out += ind.toString
  }

  def averageFitness(fitnessFun: IndType => Double)():Double = {
    //population.map(ind => future{fitnessFun(ind)}).map(f=>f()).reduceLeft[Double] {(acc, n) => acc + n} / population.size
    population.map(ind => fitnessFun(ind)).reduceLeft[Double] {(acc, n) => acc + n} / population.size
  }
  

  def bestIndFitness(fitnessFun: IndType => Double)():Double = {
    fitnessFun(nBest(1, fitnessFun)(0))
  }
  def standardDeviation(fitnessFun: IndType => Double)():Double = {
    val mean = averageFitness(fitnessFun)
    //val mean = population.map(ind => fitnessFun(ind)).reduceLeft[Double](_ + _) / population.size
    Math.sqrt(
      population.map(ind => {
        //future{Math.pow(fitnessFun(ind) - mean, 2)}
        Math.pow(fitnessFun(ind) - mean, 2)
      //}).map(f=>f()).reduceRight[Double](_ + _) / population.size
      }).reduceRight[Double](_ + _) / population.size
      )
  }

  var pop_sorted:List[IndType] = null
  def nBest(n: Int, fitnessFn: IndType => Double) = {
    if(pop_sorted == null){
      pop_sorted = population.sort((a: IndType, b: IndType) => {
      fitnessFn(a) >= fitnessFn(b)
    })
      pop_sorted.take(n)
    }else{
      pop_sorted.take(n)
    }
  }

  def :::(pop: Population[IndType]) = {
    new Population(this.population ::: pop.population, Math.max(this.gen, pop.gen))
  }

  def :::(individuals: List[IndType]) = {
    if (this.population != null)
      new Population(this.population ::: individuals, this.gen)
    else
      new Population(individuals, this.gen)
  }

  def ::(individual: IndType) = {
    if (this.population != null)
      new Population(individual :: this.population, this.gen)
    else
      new Population(List(individual), this.gen)
  }

}
