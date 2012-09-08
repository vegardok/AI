package it3708.ea

import problem.IntIndividual
import collection.mutable.{HashMap, BitSet}

/**
 * User: vegardok
 * Date: Feb 4, 2010
 */

object CrossoverFunctions {
  private val rand = new Random

  def ero(indA:IntIndividual, indB:IntIndividual):IntIndividual = {
    val mapping: HashMap[Int, Set[Int]] = new HashMap[Int, Set[Int]]()
    for(i <- 0 until indA.size)
      mapping += (i -> Set[Int]())
    for(i <- 0 until indA.size){
      if(i == 0){
        mapping(indA.genotype(i)) = mapping(indA.genotype(i)) +indA.genotype.last
        mapping(indB.genotype(i)) = mapping(indB.genotype(i)) +indB.genotype.last
      }else{
        mapping(indA.genotype(i)) = mapping(indA.genotype(i)) + indA.genotype(i - 1)
        mapping(indB.genotype(i)) = mapping(indB.genotype(i)) + indB.genotype(i - 1)
      }
      mapping(indA.genotype(i)) = mapping(indA.genotype(i)) + indA.genotype((i + 1) % (indA.size))
      mapping(indB.genotype(i)) = mapping(indB.genotype(i)) + indB.genotype((i + 1) % (indB.size))
    }

    var k:List[Int] = List[Int]()
    var n = indA.genotype(rand.nextInt(indA.size))
    for( i <- 0 until indA.size){
      for{t <- mapping
          k = t._1
          v = t._2}{
        mapping(k) = v - n
      }
      n = if(mapping(n).size > 0) {
        var fewest_neighbours:Int = mapping(n).toList(0)
        for(neighbour <- mapping(n).toList)
          if(mapping(neighbour).size < mapping(fewest_neighbours).size)
            fewest_neighbours = neighbour
        fewest_neighbours
      } else{
        val indAMinusK = indA.genotype -- k
        indAMinusK(rand.nextInt(indAMinusK.size))
      }
      k = n :: k
    }
    if(rand.nextInt(5) == 0) new IntIndividual(swapMutate(k)) else new IntIndividual(k)
  }

  /**
   * WARNING: Here be dragons
   */
  def pmx(indA:IntIndividual, indB: IntIndividual):IntIndividual = {
    var c1 = indA.genotype
    var c2 = indB.genotype
    val sliceSize =  rand.nextInt(indA.size /2)
    val sliceStartA =  rand.nextInt(indA.size - sliceSize)
    val sliceStartB =  sliceStartA // rand.nextInt(indB.size - sliceSize)
    
    val mutate = rand.nextInt(50) == 0

    def removeDups(genome:List[Int], startSlice:Int, sliceLength:Int, mapping:Map[Int, Int]):List[Int] = {
      {for{i <- 0 until genome.size} yield {
        if ((i < sliceStartA || i >= (sliceStartA + sliceLength))&&
                mapping.contains(genome(i))) //&& !genome.slice(0,cutOf).contains(mapping(genome(i))))
          mapping(genome(i))
        else
          genome(i)
      }}.toList
    }

    def mapping(slice1:List[Int], slice2:List[Int]):Map[Int, Int] = {
      val map:Map[Int, Int] = Map[Int, Int]() ++ {for{i <- 0 until slice1.length
                                                      a:Int = slice1(i)
                                                      b:Int = slice2(i)
                                                      if slice1(i) != slice2(i)}
      yield { (b -> a) }}

      Map[Int, Int]() ++ {for{t:Tuple2[Int, Int] <- map
                              k:Int = t._1
                              v:Int = dive(k, map, List[Int]())} yield
        (k -> v)
      }
    }

    def dive(start:Int, map:Map[Int, Int], visited:List[Int]):Int = {
      if(visited.contains(start) || (map.contains(start) && !map.contains(map(start))))
        map(start)
      else
        dive(map(start), map, start :: visited)
    }

    val sliceA = indA.genotype.slice(sliceStartA, sliceStartA + sliceSize)
    val sliceB = indB.genotype.slice(sliceStartB, sliceStartB + sliceSize)
    
    val aToBMapping = mapping(sliceA, sliceB)
    
    c1 = c1.slice(0, sliceStartA) ::: c2.slice(sliceStartA, sliceStartA + sliceSize) ::: c1.slice(sliceStartA + sliceSize, c1.size)
    
    c1 = removeDups(c1, sliceStartA, sliceSize, aToBMapping)

//    if(c1.sort((a,b)=> a<=b) != (0 until c1.size).toList)
//      throw new Exception("\nFeil path\n" +
//              "A: " + indA.genotype.slice(0, sliceStartA).toString + " " +
//              indA.genotype.slice(sliceStartA, sliceStartA + sliceSize) + " " +
//              indA.genotype.slice(sliceStartA + sliceSize, indA.size) + "\n"+
//              "R: " + c1.slice(0, sliceStartA).toString + " " +
//                                          c1.slice(sliceStartA, sliceStartA + sliceSize) + " " +
//                                          c1.slice(sliceStartA + sliceSize, indA.size) + "\n"+
//
//              "B: " + indB.genotype.slice(0, sliceStartB).toString + " " +
//              indB.genotype.slice(sliceStartB, sliceStartB + sliceSize) + " " +
//              indB.genotype.slice(sliceStartB + sliceSize, indB.size) + "\n"+
//              "Map\t" + aToBMapping + "\n" +
//
//              c1.sort((a, b) => a <= b) + "(" + c1.size + ")\n" +
//
//              "A:\t" + sliceA + "\n" +
//              "B:\t" + sliceB)
    if (mutate) new IntIndividual(swapMutate(c1)) else new IntIndividual(c1)
    
  }

  def copulateRandom(indA: BinaryIndividual, indB: BinaryIndividual): BinaryIndividual = {
    val mutate = rand.nextInt(50) == 0 // 2% chance for mutation
    val childGenome = new BitSet(indA.genotype.capacity)
    (0 until indA.size).foreach(i =>
      {
        val switch = rand.nextBoolean
        if (switch && indA.genotype(i))
          childGenome += i
        else if (!switch && indB.genotype(i))
          childGenome += i
      })
    new BinaryIndividual(false, {if (mutate) mutateFun(childGenome) else childGenome})
  }

  def copulate2way(indA: BinaryIndividual, indB: BinaryIndividual): BinaryIndividual = {
    val mutate = rand.nextInt(50) == 0 // 2% chance for mutation
    val childGenome = new BitSet(indA.genotype.capacity)
    val cutof: Int = rand.nextInt(indA.size)
    for (i <- 0 until indA.size) {
      if (i <= cutof && indA.genotype(i)) {
        childGenome += i
      } else if (i > cutof && indB.genotype(i)) {
        childGenome += i
      }
    }
    new BinaryIndividual(false, {if (mutate) mutateFun(childGenome) else childGenome})
  }

  private def shift(b: BitSet): BitSet = {
    var newBitSet = b.clone
    for (i <- 0 to b.capacity) {
      if (b(i)) {
        newBitSet += (i + 1 % b.capacity)
      }
    }
    newBitSet
  }

  /*
 Flips 1 to 10% of the total bits in see(b)
  */
  private def flipRandom(b: BitSet): BitSet = {
    val newBitSet = b.clone
    for (i <- 0 to rand.nextInt({b.capacity * 0.1}.toInt +1)) {
      val flipIndex = rand.nextInt(newBitSet.capacity)
      if (newBitSet(flipIndex))
        newBitSet -= flipIndex
      else
        newBitSet += flipIndex
    }
    newBitSet
  }

  private def swapMutate[T](l:List[T]) = {
    val a = l.toArray
    for(i <- 0 until rand.nextInt(l.size / 10)){
      val index1:Int = rand.nextInt(l.length)
      val index2:Int = rand.nextInt(l.length)
      val temp:T = a(index1)
      a(index1) = a(index2)
      a(index2) = temp
    }
    a.toList
  }

  /*
The function that does a mutation on a individual on a coupling
  */
  def mutateFun = flipRandom _

}
