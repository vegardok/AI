package it3708.ea.problem

import collection.BitSet
import it3708.util.Utils
import java.util.Locale
import it3708.ea._

/**
 * User: vegardok
 * Date: Feb 1, 2010
 */

//abstract class ColonelBlotto(logPath: String) extends EAProblem[BinaryIndividual] {
//  def this() = {
//    this ("onemax.log.dat")
//  }
//
//
//  def logFilePath = logPath
//  val size_of_num = 4
//  def battles = 5
//  val resources = 1
//
//  def R = 0.5
//  def L = 1.0 / (battles * 2)
//
//  def convert = Utils.binToInt _
//
//  def individualSize = {battles * size_of_num}
//
//  def populationSize = 20
//
//  def simulateGenerations = {50}
//
//  def selectionStrategy(p:Population[Individual]) = {
//    SelectionStrategies.boltzmannSelection(fitness)(p)
//  }
//  def nextGeneration(p:Population[Individual]) ={
//    SelectionProtocols.generationMixing(fitness, selectionStrategy)(p)
//  }
//
//  private def strategyEntropy(l:List[Double]):Double = {
//    l.map[Double](d => if (d==0.0) 0.0 else Math.abs((d/resources) * Math.log(d/resources) / Math.log(2))).reduceRight[Double](_+_)
//  }
//
//  override val logFunctions = super.logFunctions.:::(
//    List(
//      ('d_strategy_entropy, ((p:Population[Individual]) => {
//        p.population.map[Double]((ind)=>
//          strategyEntropy(phenotype( ind ))).reduceLeft[Double](_+_)  / p.population.length
//      })),
//      ('title, (p:Population[Individual]) => {
//        var title = "\""
//        for{index <- 0 until individualSize
//            if index % size_of_num == 0
//            ind = p.nBest(1, fitness)(0)} {
//          title = title.concat(convert({
//            for{j <- index until (index + size_of_num)} yield {
//              ind.genotype(j)
//            }}.toList).toString) + " "
//          title + "\""
//        }
//        title
//      })))
//
//
//  def phenotype(ind: BinaryIndividual): List[Double] = {
//
//    if(ind.phenotype != null)
//      ind.phenotype.asInstanceOf[List[Double]]
//    else{
//      if(ind.genotype.capacity != individualSize)
//        throw new Exception("WTH?")
//      val ints: List[Int] = {for{index <- 0 until individualSize
//                               if index % size_of_num == 0} yield {
//        convert({
//        for{j <- index until (index + size_of_num)} yield {
//          ind.genotype(j)
//        }}.toList)}}.toList
//      val sum = ints.reduceRight[Int](_ + _)
//      ind.phenotype = for (i <- ints) yield {i.toDouble / sum} * resources
//      ind.phenotype.asInstanceOf[List[Double]]
//    }
//  }
//
//
//  def fitness(ind: BinaryIndividual): Double = {
//    // win = 2, tie = 1, loose = 0
//    def battle(a: List[Double], opponent: List[Double]): Int = {
//      var win = 0;
//      var loose = 0
//      var tie = 0
//      var strength = 1.0
//      var surplus_resources = 0.0
//      var result_string = ""
//      for (i <- 0 until battles){
//        val extra:Double = {surplus_resources / (battles - i) } * R
//        //result_string = result_string.concat("(%f1.3 vs %f1.3) ".format( {(a(i) + extra) * strength}, opponent(i)))
//        if ((a(i) + extra) * strength > opponent(i)){
//          surplus_resources += { (a(i) - opponent(i)) + extra}
//          win += 1
//          //println({((a(i) + extra) * strength)} + " vs " +  opponent(i) +" : "  +{((a(i) + extra) * strength) - opponent(i)})
//        }
//        else if ((a(i) + extra) * strength < opponent(i)){
//          strength -= L
//          loose += 1
//        } else {
//          tie += 1
//        }
//      }
//      if (win > loose)
//        2
//      else if (win == loose)
//        1
//      else
//        0
//    }
//    var score = 0.0
//    for{opponent <- population.population if opponent != ind} {
//      score += battle(phenotype(ind), phenotype(opponent))
//    }
//
//    val fitness = score / (population.size * 2)
//    if(fitness < 0 || fitness > 2)
//      throw new Exception("Negativ fitness? " + fitness)
//    fitness + 0.001
//  }
//}
