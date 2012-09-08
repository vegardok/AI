package it3708.ea

import it3708.util._
import java.io.{PrintStream, File}
import javax.naming.directory.AttributeInUseException

class Simulator[T <: Individual](problem: EAProblem[T]) {
  val t1 = System.currentTimeMillis
  var currentPopulation: Population[T] = problem.population
  var logger = new ResultLogger(problem.logFilePath)
  val progress_dash_interval = Math.max(1, problem.simulateGenerations / 100)

  val verbose = false
  print("Progress (" + problem.simulateGenerations + " gens) : ")
  try {
    for (generation_id <- 0 until problem.simulateGenerations) {
      problem.logFunctions.foreach(logTuple => {
        logger.addResult(logTuple._1, logTuple._2(currentPopulation))
      })
      if (generation_id % progress_dash_interval == 0 && !verbose)
        print("-")
      if (verbose)
        println("%.4f".format(problem.fitness(currentPopulation.nBest(1, problem.fitness)(0))) + "    " +
                currentPopulation.nBest(1, problem.fitness)(0).genotype.toString)
      if(generation_id % 10 == 0 && problem.logFunctions(Symbol("a_avg-fitness"))(currentPopulation).asInstanceOf[Double] >= 1){
        val e =  new LoopDoneException("Optimal fitness, gen " + generation_id)
        e.gen = generation_id
        throw e
      }

      currentPopulation = problem.nextGeneration(currentPopulation)
      problem.pop = currentPopulation
      if(generation_id == problem.simulateGenerations -1)
        println("Best sollution: " + problem.fitness(currentPopulation.nBest(1, problem.fitness)(0)))
    }
  } catch {
    case e:LoopDoneException => {
      println("Optimal TSP!")
      val path = "optimal_"+e.gen+"_"+problem.logFilePath
      logger.logPath = path
      val file = new File(path)
      file.delete
      file.createNewFile
      logger.print(new PrintStream(file))
    }
    case e => {System.err.println("\nUncaught exception at gen, breaking an d logging\n" + e.printStackTrace)
      val file = new File(problem.logFilePath)
      file.delete
      file.createNewFile
      logger.print(new PrintStream(file))
      throw new Exception("foo")}
  }


  val t2 = System.currentTimeMillis
  println
  println("Simulated %d generations in %.1f seconds".format(problem.simulateGenerations, ((t2 - t1) / 1000.0)))

  val file = new File(problem.logFilePath)
  file.delete
  file.createNewFile
  logger.print(new PrintStream(file))
}

