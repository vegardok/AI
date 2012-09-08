package it3708.ea.problem

import it3708.ea.{BinaryIndividual, EAProblem}

/**
 * User: vegardok
 * Date: Jan 31, 2010
 */

//abstract class ZebraGene(logPath: String) extends EAProblem {
//  def this() = {
//    this ("zebralog.log.dat")
//  }
//
//  def logFilePath = logPath
//
//  def individualSize = 1000
//
//  def populationSize = 1000
//
//  def phenotype(ind:BinaryIndividual) = {
//    ind.genotype
//  }
//  def fitness(ind: BinaryIndividual): Double = {
//    var count = 0.0
//    var last = ind.genotype(0)
//    (1 until ind.size).foreach(i => {
//      if (last != ind.genotype(i))
//        count += 1
//      last = ind.genotype(i)
//    })
//    count / ind.size
//  }
//}