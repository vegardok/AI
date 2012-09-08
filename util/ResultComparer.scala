package it3708.util

import java.io.{PrintStream, File}

/**
 * User: vegardok
 * Date: Feb 4, 2010
 */

class ResultComparer(title:String, results: List[ResultLogger]) {
  import scala.collection.mutable.Map
  val keys = results.map(r => r.result.keySet).flatten(i => i.toList)
  val results_collected_by_key = Map[Symbol, Map[String, List[Any]]]()
  for{key <- keys} {
    for{result <- results} {
      if (results_collected_by_key.keys.contains(key))
        results_collected_by_key += (key -> {results_collected_by_key(key) + (result.logPath -> result.result(key))})
      else
        results_collected_by_key += (key -> Map(result.logPath -> result.result(key)))
    }
  }

  val outputs = Map[String, StringBuilder]()

  for {tuple <- results_collected_by_key
       key = tuple._1
       result = tuple._2
       if key != 'title} {
    var stop = false
    var firstRun = true
    val output = new StringBuilder
    while (!stop) {
      stop = true
      if (firstRun) {
        // print headers
        for (tuple <- result )
          output.append("\"" + //key.toString.substring(2).replace(".log.dat","").replace("'","").replace("_", " ")+
                  tuple._1.toString.replace("'", "").replace("_", " ").replace(".log.dat", "") + "\"" + "\t")
        output.append("\n")
        firstRun = false
      }
      for(resultTuple <- result){
        if(resultTuple._2.size > 0){
          output.append(resultTuple._2(0) + "\t")
          result += (resultTuple._1 -> resultTuple._2.drop(1))
          stop = false
        }
      }
      output.append("\n")
    }
    outputs += (title + "_"+key+".log.dat" -> output)
  }

  for{tuple <- outputs
      filename = tuple._1}{
    val out = new PrintStream( new File(filename))
    println("Printing log to: " + tuple._1)
    out.print(tuple._2.toString)
    out.close
    val key_placement = if(!filename.contains("std")) "set key bottom right;" else ""
    var gnuplot_command = "reset;" +key_placement+" set xlabel \"Generation\";set title \"" + title + " " +
            filename.split("_")(2).replace(".log.dat", "") + "\"" +
            ";set terminal png size 600, 700;set key autotitle columnhead; set output \"" +
            filename.replace("log.dat", "png").replace("'", "").replace("_","-") +"\"; plot "
    
    for{column <- tuple._2.toString().substring(1).split("\n")(0).toString.split("\t")
        titles = tuple._2.toString().substring(1).split("\n")(0).toString.split("\t")}{
      //'c_std_deviation_populatin_'full_replacement_'sigma_onemax.log.dat	'c_std_deviation_populatin_'generation_mixing_'fitness_prop_onemax.log.dat	'c_std_deviation_populatin_'full_replacement_'rank_onemax.log.dat	'c_std_deviation_populatin_'generation_mixing_'rank_onemax.log.dat	'c_std_deviation_populatin_'over_production_'sigma_onemax.log.dat	'c_std_deviation_populatin_'full_replacement_'fitness_prop_onemax.log.dat	'c_std_deviation_populatin_'generation_mixing_'sigma_onemax.log.dat	'c_std_deviation_populatin_'over_production_'rank_onemax.log.dat	'c_std_deviation_populatin_'over_production_'fitness_prop_onemax.log.dat
      val delimiter = if(titles.last ==  column) "" else ","
      gnuplot_command = gnuplot_command.concat ("\"" + filename
              + "\" u " + {titles.indexOf(column) + 1 } +
              " with lines " + delimiter)
    }
    val gnu = new PrintStream(new File(filename.replace("log.dat", "gnu").replace("'", "")))
    gnu.print(gnuplot_command)
    gnu.close
    System.out.println("gnuplot " + filename.replace("log.dat", "gnu").replace("'", ""))
    Runtime.getRuntime().exec("/usr/local/bin/gnuplot " + filename.replace("log.dat", "gnu").replace("'","")).waitFor

    
  }

  

}