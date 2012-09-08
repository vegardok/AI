package it3708.util

import collection.jcl.HashMap
import java.io.{BufferedWriter, File, PrintStream}

/**
 * User: vegardok
 * Date: Jan 31, 2010
 */


/**
 * This class expects there to be the same amount
 * of results for each result "type"
 */
class ResultLogger(p:String) {
  val result = new HashMap[Symbol, List[Any]]()
  var logPath = p
  def addResult(s: Symbol, res: Any) = {
    result.get(s) match {
      case None => {
        result + Tuple(s, List(res))
      }
      case Some(l: List[Any]) => {
        result + Tuple(s, l ::: List(res))
      }
    }
  }

  /*x
   * Gnuplot command:
   * plot "zebralog.dat" u 1 with lines ti "Average zebra", "zebralog.log.dat" u 2 with lines ti "Best zebra", "zebralog.log.dat" u 3 ti "Std_dev zebra" with lines, "onemax.log.dat" u 1 with lines ti "Average OneMax", "onemax.log.dat" u 2 with lines ti "Best OneMax", "onemax.log.dat" u 3 with lines ti "Std_dev OneMax"

   */
  def print(out:PrintStream):Unit = {
    /*
    Sorts the result alphabetically from the the symbol name
     */
    val print_to_file = try{
      val p = Runtime.getRuntime().exec("/usr/local/bin/gnuplot")
      p.destroy
      true
    }catch{
      case e => false
    }

    val _result = result.clone
    
    val result_sorted = _result.toList.sort((a: (Symbol, List[Any]), b: (Symbol, List[Any])) => {
        a._1.toString.compareTo(b._1.toString) <= 0
      })
    val output_result = new StringBuilder
    var stop = false
    var firstRun = true
    while (!stop) {
      stop = true
      _result.toList.sort((a: (Symbol, List[Any]), b: (Symbol, List[Any])) => {
        a._1.toString.compareTo(b._1.toString) <= 0
      }).foreach(resultTuple => {
        if (firstRun) {
          output_result.append("\"" + resultTuple._1.toString.substring(2).replace("'","").replace("_", " ").replace("-", " ") + "\"\t")
          stop = false
        }
        if (resultTuple._2.size > 0 && !firstRun) {
          stop = false
          output_result.append(resultTuple._2(0) + "\t")
          _result + Tuple(resultTuple._1, resultTuple._2.drop(1))
        }
      })
      firstRun = false
      output_result.append("\n")
    }
    out.print(output_result.toString)
    out.close()
    val xtitle = if(_result.keySet.contains('title)) "u %d:xticlabel(%d)" else " u %d"

    var command = "reset;set key box; set xlabel \"Best individual\";set terminal png size 600, 600;" +
            "set output \"" + logPath.replace("'", "").replace("_","-").replace("log.dat", "png") + "\";" +
            "set title \"" + logPath.replace("'", "\\'") + "\";" +
            "set xtics rotate by 90 nomirror;" +
            "set key autotitle columnhead;plot "

    for{res_tuple <- result_sorted if res_tuple._1 != 'title}{
      val separator = if(res_tuple == result_sorted.last) "" else ", "
      command = command.concat( "\"" + logPath + "\""  + xtitle.format(result_sorted.indexOf(res_tuple)+1,
        result_sorted.length) + " with lines" + separator)
    }
    command = command.concat("\"")
    if(print_to_file){
      val gnu = logPath.replace("'","").replace("log.dat", "gnu")
      val w = new PrintStream(new File(gnu))
      w.print(command)
      w.close
      Runtime.getRuntime().exec("/usr/bin/gnuplot "+ gnu).waitFor
    }
  }

}

