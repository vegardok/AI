package it3708.ea

/**
 * User: vegardok
 * Date: Feb 14, 2010
 */

trait Individual{
  def genotype:Object
  def fitness(f: Individual => Double):Double
  def size:Int  
}