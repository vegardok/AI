package it3708.ea.problem

import collection.mutable.ArrayBuffer
import it3708.ea.{CrossoverFunctions, Individual, BinaryIndividual}

/**
 * User: vegardok
 * Date: Feb 14, 2010
 */

class IntIndividual(randomGenome:Boolean, size:Int, genome:List[Int]) extends Individual{
  def this(size:Int) = this(true, size, List[Int]())
  def this(genome:List[Int]) = this(false, genome.length, genome)

  private val rand = new Random

  private var _genome = genome
  if(randomGenome){
    val range = new ArrayBuffer[Int]() 
    range ++= {0 until size}.toList
    _genome = {for{foo <- 0 until range.size
                 next = rand.nextInt(range.size)
    } yield {
        range.remove(next)
      }}.toList
  }

  def genotype:List[Int] = {
    _genome
  }

  var cache: Double = -1

  def fitness(fitnessFun: Individual => Double) = {
    cache match {
        case -1 => {
          cache = fitnessFun(this)
          cache
        }
        case _ => cache
    }
  }
  def size() = _genome.length

  def copulate(other:IntIndividual):IntIndividual = {
    CrossoverFunctions.pmx(this, other)
  }
  
}