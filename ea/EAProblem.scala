package it3708.ea

/**
 * User: vegardok
 * Date: Jan 31, 2010
 */


trait EAProblem[IndType <: Individual] {
  type Ind = IndType
  def averageFitness(pop: Population[IndType]): Double = {
    pop.averageFitness(fitness)
  }

  def bestIndividualFitness(pop: Population[IndType]): Double = {
    fitness(pop.nBest(1, fitness)(0))
  }

  private val rand = new Random

  def randInt = it3708.util.Utils.randInt _


  /*
  Each tuple spesifies something that is logged to the final
  log file. The tuple consist of the name of the log column
  in the file and a function that takes a population and returns
  something that is written to the file. The columns are ordered
  alphabetically by the symbol name.

  Eksample result, 3 default functions:
  #'a_average	'       b_best_ind	'c_std_deviation
  0.4996999999999999	0.61       	0.05281013160369894
  0.5547455445544551	0.61	      0.05186305377083598
  0.6078603960396035	0.61	      0.021288790609776256
  0.6063148514851481	0.61	      0.026135818107310495
  ....
   */
  def logFunctions: Map[Symbol, Population[IndType] => Any] = {
    Map(

      //    (Symbol("c_fitness-std-dev-"), standardDeviation(fitness) _),
      (Symbol("a_avg-fitness"), averageFitness _),
      (Symbol("b_best-ind-fitness"), bestIndividualFitness _))
  }
  
  

  /*
  How many rounds a simulation is run. This enables a spesific problem
  to configure the simulator without changing the simulator code.
  100 is a "random" default for testing.
   */
  def simulateGenerations:Int;

  /*
  The function that does the beautifull act of combining two individuals to one.
  TODO: få denne inn på en nyttig måte
   */
  //def copulate(a: IndType, b: IndType): IndType = CrossoverFunctions.copulateRandom(a, b)

  /*
  Choose a strategy from SelectionStrategies or implement
  a new one :)
   */
  def selectionStrategy(p: Population[IndType]): List[(IndType, Double)]


  /*
  A function that takes a population and does all the
  steps to create the next generation
   */
  def nextGeneration(p: Population[IndType]): Population[IndType]


  /*
 The fitness of individual
  */
  def fitness(i: IndType): Double

  /*
  The number of individuals in the initial population
   */
  def populationSize: Int

  /*
  Each individual in the population have a see(individualSize) bit genome
   */
  def individualSize: Int

  /*
 Defines the path of the log file that is created after a
  simulator run
  */
  def logFilePath: String

  def phenotype(ind: IndType): Object

  def copulate(a:IndType, b:IndType):IndType

  var pop:Population[IndType]
  
  def population(): Population[IndType] = {
    if(pop == null)
      throw new Exception("Init population!")
    pop
  }
}
