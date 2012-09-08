package it3708.ea.problem

import it3708.util.TSPDataset
import it3708.ea._
import collection.jcl.HashMap

/**
 * User: vegardok
 * Date: Feb 14, 2010
 */


abstract class TSP[IndType <: Individual] extends EAProblem[IndType] {

  /*
  Have to be implemented by subclass:
   */
  //def copulate(a:Ind, b:Ind)
  
  def phenotype(ind:Ind):List[Int]
  def optimal:Double
  def dataSet:TSPDataset
  

  /*
  Common TSP functions
   */

  def populationSize = 500
  def simulateGenerations = {1000}


  var cache = new HashMap[String, Double]()
  def fitness(ind:Ind):Double = {
    val pheno =  phenotype(ind)
    val key = pheno.toString
    if(cache.contains(key)){
      cache(key)
    }
    else{
      cache = cache + (key -> optimal.toDouble / dataSet.length(pheno))
      cache(key)
    }
  }
  def selectionStrategy(p:Population[Ind]) = {
    SelectionStrategies.rankSelection(fitness, population)
  }
  def nextGeneration(p:Population[Ind]) ={
    SelectionProtocols.generationMixing(fitness, selectionStrategy, this)
  }

}