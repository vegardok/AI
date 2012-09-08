package it3708.ea.problem

import it3708.ea.{BinaryIndividual, EAProblem}

/**
 * User: vegardok
 * Date: Feb 1, 2010
 */

//abstract class NumberSorter(logPath: String) extends EAProblem {
//  def this() = {
//    this ("numbersorter.log.dat")
//  }
//
//  def logFilePath = logPath
//
//  def individualSize = 1024
//
//  def populationSize = 1000
//
//  def phenotype(ind: BinaryIndividual): List[Int] = {
//    {
//      for{i <- 0 until individualSize
//          if i % size_of_num == 0} yield {
//        bitsToInt(
//          {for{j <- i until i + size_of_num} yield {
//              ind.genotype(j)
//            }
//          }.toArray)
//      }
//    }.toList
//  }
//
//  // how many bits pr number
//  private var size_of_num = 8
//  private var nums = individualSize / size_of_num
//
//  def fitness(ind: BinaryIndividual): Double = {
//    val numList = phenotype(ind)
//    var score = 0
//    for (i <- 1 until numList.size)
//      if(numList(i) == (numList(i-1)+1))
//        score +=1
//    score * 1.0 / numList.size
//  }
//
//  private def bitsToInt(a: Array[Boolean]) = {
//    var res = 0.0
//    for (i <- 0 until size_of_num) {
//      if (a(i))
//        res += Math.pow(2, i)
//    }
//    res.toInt
//  }
//}