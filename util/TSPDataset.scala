package it3708.util

import java.io.{BufferedReader, FileReader}
import collection.mutable.{HashMap, Map}

/**
 * User: vegardok
 * Date: Feb 14, 2010
 */

class TSPDataset(datasetPath:String, _optimal:Double){
  def this() = {
    this(null, 0)
  }
  var optimal = _optimal
  var dimensions = -1
  var cities:Map[Int, Tuple2[Double,Double]] = Map[Int,  Tuple2[Double,Double]]()
  var name = ""
  val path = datasetPath

  if(datasetPath != null){
    val file = new BufferedReader(new FileReader(datasetPath))
    var line = file.readLine
    while(!line.contains("NODE_COORD_SECTION")){
      if(line.contains("NAME")) name = line.split(":").last.trim
      if(line.contains("DIMENSION")) dimensions = Integer.parseInt(line.split(":").last.trim)
      line = file.readLine
    }
    for(i <- 0 until dimensions){
      val line = file.readLine.split(" ")
      cities += (i -> Tuple2(java.lang.Double.parseDouble(line(1)),
        java.lang.Double.parseDouble(line(2))))
    }
  }
  def length(path:List[Int]) = {
    import scala.actors.Futures._
//    if(path.sort((a, b) => a <=b) != (0 until path.length).toList)
//      throw new Exception("Feil path" + path.sort((a, b) => a <= b))

    {for{i <- path
         next = path((path.indexOf(i) + 1) % path.length)} yield
    //  distance(i,next)
      future{distance(i, next)}
    }.map[Long](f => f()).reduceLeft[Long](_ + _)
    //}.reduceLeft[Long](_ + _)
  }

  def distance(a:Int, b:Int) = {
    if(a == b)
      throw new Exception("Samme by")
    def longitude(index:Int):Double = {
      cities(index)._1
    }
    def latitude(index:Int):Double = {
      cities(index)._2
    }
    import java.lang.Math._

    sqrt(pow(abs(latitude(b) - latitude(a)), 2) + pow(abs(longitude(b) - longitude(a)), 2)).toLong
  }

  def scaled() = {
    val biggestX = cities.map[Double](_._2._1).reduceRight[Double]((a:Double, b:Double) => if(a > b) a else b)
    val biggestY = cities.map[Double](_._2._2).reduceRight[Double]((a:Double, b:Double) => if(a > b) a else b)
    val smallestX = cities.map[Double](_._2._1).reduceRight[Double]((a:Double, b:Double) => if(a < b) a else b)
    val smallestY = cities.map[Double](_._2._2).reduceRight[Double]((a:Double, b:Double) => if(a < b) a else b)

    val newSet = new TSPDataset(null, optimal);
    cities.foreach(t => {
      newSet.cities += Tuple2(t._1,
        Tuple2[Double, Double](( t._2._1 - smallestX)  / (biggestX - smallestX),
          (t._2._2 - smallestY) / (biggestY - smallestY)))
    })
    newSet.dimensions = dimensions
    newSet
  }
  
}