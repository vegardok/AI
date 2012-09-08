package it3708.ea

/**
 * User: vegardok
 * Date: Feb 4, 2010
 */
object SelectionProtocols{
  private val parentPercent = 0.5
  private val rand = new Random
  

  private def casino[Ind <: Individual](pop: Population[Ind],
                     numChildren: Int,
                     selectionStrategy: (Population[Ind]) => List[(Ind, Double)],
                     copulationStrategy: (Ind, Ind)=> Ind ): Population[Ind] = {

    def findIndividual[Ind <: Individual](rouletteWheel: List[Tuple2[Ind, Tuple2[Double, Double]]], score: Double): Ind = {
      rouletteWheel.find((i) => {
        score >= i._2._1 && score <= i._2._2
      }) match {
        case Some(ind) => ind._1
        case _ => throw new Exception("Did not find ind " + score + "\n")
      }
    }


    def roulette(inds: List[Tuple2[Ind, Tuple2[Double, Double]]]): Ind= {
      val parent1 = findIndividual(inds, rand.nextDouble() * inds.last._2._2)
      var parent2 = parent1
      var i = 0
      while (parent2 == parent1 && i < 10) { // parent1 vil pare seg med parent1, den er antatligvis den eneste med fitness != 0
        i += 1
        parent2 = findIndividual(inds, rand.nextDouble() * inds.last._2._2)
      }
      if (parent1 == parent2)
        println("Selvtukt! " + inds.length + " " + inds.map(t => t._2))
      copulationStrategy(parent1, parent2)
    }

    def createRouletteWheel(rankedIndividuals: List[(Ind, Double)]) = {
      var accl = 0.0
      for{tupel <- rankedIndividuals} yield {
        val temp = accl
        accl += tupel._2
        (tupel._1, (temp, accl))
      }
    }
    import scala.actors.Futures._

    val rouletteWheel2 = createRouletteWheel(selectionStrategy(pop))

    val children:List[Ind] = {for{i <- (0 until numChildren)} yield
      future{roulette(rouletteWheel2)}}.map(_()).toList
    new Population[Ind](children.toList, pop.gen + 1)
  }

  def fullReplacement[Ind <: Individual](fitness: (Ind) => Double,
                      selectionStrategy: (Population[Ind]) => List[(Ind, Double)],
                      p: EAProblem[Ind]) = {
    def copulate = p.copulate _
    casino[Ind](p.population, p.population.size, selectionStrategy, p.copulate _)
  }

  def overProduction[Ind <: Individual](fitness: (Ind) => Double,
                     selectionStrategy: (Population[Ind]) => List[(Ind, Double)],
                     p: EAProblem[Ind]) = {
    def copulate = p.copulate _
    new Population[Ind](casino[Ind](p.population,
      p.population.size * 2, selectionStrategy,
      p.copulate _ ).nBest(p.population.size, fitness), p.population.gen)
  }

  def generationMixing[Ind <: Individual](fitness: (Ind) => Double,
                       selectionStrategy: (Population[Ind]) => List[(Ind, Double)],
                      p: EAProblem[Ind]) = {

    val children = casino[Ind](p.population, p.population.size, selectionStrategy, p.copulate _)
    val both = p.population.:::(children)
    new Population[Ind](both.nBest(p.population.size, fitness), p.population.gen)
  }
  
  
  //
  //[Symbol, (Population) => Population]
  val selectionProtocolMethods = Map(
    (Symbol("full-rep-prot") -> fullReplacement _),
    (Symbol("over-prod-prot") -> overProduction _),
    (Symbol("gen-mix-prot") -> generationMixing _))
}

