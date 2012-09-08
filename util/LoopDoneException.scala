package it3708.util
import java.lang.Exception

/**
 * User: vegardok
 * Date: Feb 21, 2010
 */

class LoopDoneException(message:String) extends Exception{
  var msg = message
  var gen = -1
}