package it3708.ea.problem

import it3708.util.TSPDataset
import it3708.ea.{Population, CrossoverFunctions, EAProblem}

/**
 * User: vegardok
 * Date: Feb 18, 2010
 */

class TSP_ERO(dataset:TSPDataset, opt:Double) extends TSP[IntIndividual] {
  def dataSet = {dataset}
  def optimal = {opt}
  def copulate(a:Ind, b:Ind) = {
    CrossoverFunctions.ero(a, b)
  }
  def individualSize = {dataset.dimensions}
  def logFilePath = {dataSet.name + "tsp-ero.log.dat"}
  def phenotype(ind:Ind):List[Int] = {
    ind.genotype
  }
  var pop = new Population[Ind](populationSize, 0, individualSize,
    {for(i<- 0 until populationSize) yield {new Ind(individualSize)}}.toList)

}
