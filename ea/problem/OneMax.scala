package it3708.ea.problem
import collection.mutable.BitSet
import it3708.ea._

/**
 * User: vegardok
 * Date: Jan 31, 2010
 */


class OneMax(logPath: String) extends EAProblem[BinaryIndividual] {
  def this() = {
    this ("onemax.log.dat")
  }
  var pop = new Population[BinaryIndividual](populationSize, 0, individualSize,
    {for(i<- 0 until populationSize) yield {new BinaryIndividual(individualSize)}}.toList)


  def logFilePath = logPath

  def individualSize = 100

  def populationSize = 100

  def simulateGenerations = 100


  def phenotype(ind:BinaryIndividual) = {
    ind.genotype
  }

  def fitness(ind: BinaryIndividual): Double = {
    ind.genotype.size.toFloat / ind.genotype.capacity
  }

  def copulate(a:BinaryIndividual, b:BinaryIndividual):BinaryIndividual = {
    CrossoverFunctions.copulate2way(a, b)
  }

  val title = "onemax"
  
  def selectionStrategy(p:Population[BinaryIndividual]) = {
    SelectionStrategies.sigmaScalingMatingSelection(fitness, population)
  }
  def nextGeneration(p:Population[BinaryIndividual]) ={
    SelectionProtocols.generationMixing(fitness, selectionStrategy, this)
  }

}