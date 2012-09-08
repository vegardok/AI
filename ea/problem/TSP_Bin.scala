package it3708.ea.problem

import it3708.util.{Utils, TSPDataset}
import it3708.ea.{CrossoverFunctions, Population, BinaryIndividual}
import collection.jcl.{HashMap, ArrayList}

/**
 * User: vegardok
 * Date: Feb 18, 2010
 */

class TSP_Bin(data: TSPDataset, opt: Double) extends TSP[BinaryIndividual] {
  val intSize = Math.ceil(Math.log(dataSet.dimensions) / Math.log(2)).toInt

  override def individualSize = {dataSet.dimensions * intSize}
  def logFilePath = {data.name + "tsp-bin.log.dat"}
  def optimal: Double = {opt}
  def dataSet: TSPDataset = {data}
  
  def copulate(a: Ind, b: Ind) = {
    CrossoverFunctions.copulate2way(a, b)
  }

  var pop = new Population[Ind](populationSize, 0, individualSize,
      {for(i<- 0 until populationSize) yield {new Ind(individualSize)}}.toList)
  
  var pheno_cache = new HashMap[String, List[Int]]()
  def phenotype(ind: Ind): List[Int] = {
    val key = ind.genotype.toString
    if(pheno_cache.contains(key))
      pheno_cache(key)
    else{
      val stack = new ArrayList[Int]()
      stack.addAll(0 until data.dimensions)
      val res = for{i <- 0 until individualSize by intSize
                    index = Utils.binToInt({
                      for (i <- i until i + intSize) yield
                        {if (ind.genotype(i)) true else false}
                    }.toList) % stack.size} yield {
        stack.remove(index)
      }
      pheno_cache += (key -> res.toList)
      pheno_cache(key)
    }
  }
}