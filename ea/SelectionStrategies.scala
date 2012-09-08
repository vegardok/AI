package it3708.ea

/**
 * User: vegardok
 * Date: Feb 4, 2010
 */
object SelectionStrategies {
  import it3708.ea._

  def sigmaScalingMatingSelection[Ind <:Individual](fitness: (Ind) => Double, pop: Population[Ind]): List[(Ind, Double)] = {
    val std_dev = pop.standardDeviation(fitness)
    val avg_fitness = pop.averageFitness(fitness)
    for{ind <- pop.population} yield
      (ind, 1 + fitness(ind) - avg_fitness / 2 * std_dev)
  }

  def boltzmannSelection[Ind <:Individual](fitness: (Ind) => Double, pop: Population[Ind]): List[(Ind, Double)] = {
    val startT = 5
    val simulateGenerations = 500 // number of generations before t = 1
    val t = Math.max(1, ((1 - (pop.gen * 1.0 / simulateGenerations)) * (startT - 1)) + 1)

    val populationBoltzAvg = pop.population.map(ind => {
      Math.pow(Math.E, fitness(ind) / t)
    }).reduceRight[Double](_ + _) / pop.size
    for{ind <- pop.population} yield
      (ind, Math.pow(Math.E, fitness(ind) / t) / populationBoltzAvg)
  }

  def fitnessProportionateSelection[Ind <:Individual](fitness: (Ind) => Double, pop: Population[Ind]): List[(Ind, Double)] = {
    val fitness_average = pop.averageFitness(fitness)
    if(fitness_average == 0.0)
      pop.population.map(ind => {
        (ind, 1.0)
      })
    else
      pop.population.map(ind => {
        (ind, fitness(ind) / fitness_average)
      })
  }

  def rankSelection[Ind <:Individual](fitness: (Ind) => Double, pop: Population[Ind]): List[(Ind, Double)] = {
    val pop_sorted = pop.nBest(pop.size, fitness)
    val min = 0.5
    val max = 1.5
    pop_sorted.map(ind => {
      (ind, min + {{max - min}.toDouble * {pop_sorted.indexOf(ind).toDouble / {pop_sorted.size - 1}.toDouble}})
    })

  }
//
//  def tournamentSelection(fitness: Individual => Double)(pop:Population[Individual]):List[(Individual, Double)] = {
//    import scala.collection.mutable.Map
//    val rand = new Random
//    // the "cut of", a random double between 0 and 1 is drawn, if ut is under epsilon a random draw
//    // is done, over and the ind with the best fitness is choosen.
//    val epsilon = 1/50
//    val K = 10
//    val score = Map[Individual, Double]()
//    pop.population.foreach(i => score += (i -> 0.0))
//    for(i <- 0 until pop.size){
//      var pop_copy:List[Individual] = pop.population
//      val random_draw = rand.nextDouble < epsilon
//      val local_group = for(foo <- 0 until K) yield {
//        val lucky_ind = pop_copy(rand.nextInt(pop_copy.size))
//        pop_copy = pop_copy.diff(List(lucky_ind))
//        lucky_ind
//      }
//      if(random_draw)
//        score(local_group(rand.nextInt(local_group.size))) += 1.0
//      else
//        score(local_group.toList.sort((a: Individual, b: Individual) => {
//          a.fitness(fitness) >= b.fitness(fitness)
//        })(0)) += 1
//    }
//    score.toList
//  }

  //[Symbol, (Population) => List[(Individual, Double)]]
//  val selectionStrategyMethods = Map(
//    (Symbol("sigma-sel") -> sigmaScalingMatingSelection _),
//    (Symbol("boltzmann-sel") -> boltzmannSelection _),
//    (Symbol("rank-sel") -> rankSelection _),
//    (Symbol("fitness-prop-sel") -> fitnessProportionateSelection _),
//    (Symbol("tournament-sel") -> tournamentSelection _))
}