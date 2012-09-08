package it3708.ea

import collection.mutable.BitSet
import java.security.SecureRandom

/**
 * User: vegardok
 * Date: Jan 31, 2010
 */
class BinaryIndividual(randomGenome: Boolean,
                 genome: BitSet) extends Individual {
  def this(genomeLength: Int) = {this (true, new BitSet(genomeLength))}

  def this(genome: BitSet) = {this (false, genome)}

  if(genome.capacity == 0)
    throw new Exception("foo")

  private var _genotype = genome
  def size = {genome.capacity}
  var phenotype:Object = null
  
  if (randomGenome) {
    _genotype = new BitSet(genome.capacity)
    val rand = new SecureRandom
    for (i <- 0 until _genotype.capacity) {
      if (rand.nextBoolean) {
        genotype += i
      }
    }
  }

  def id = {
    super.toString
  }

  def genotype: BitSet = {_genotype}

  def fitness(fitnessFun: Individual => Double) = {
    fitnessFun(this)
  }

  override def toString() = {
    import java.lang.StringBuilder
    var b = new StringBuilder("BinaryIndividual " + id + " : [")
    for (i <- 0 until genome.capacity) {
      if (genotype(i))
        b.append("1, ")
      else
        b.append("0, ")
    }
    b.append("]\n")
    b.toString()
  }


  def copulate(other:BinaryIndividual):BinaryIndividual = {
    CrossoverFunctions.copulate2way(this, other)
  }
}
