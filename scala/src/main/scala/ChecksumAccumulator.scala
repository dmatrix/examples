/**
  * Created by jules on 3/12/16.
  */
import scala.collection.mutable.Map

class ChecksumAccumulator {

  private var sum = 0
  /**
    * a method with side effect. the value returned is not the sum, but Unit. Indeed, as such
    * it's a procedure, not a pure function in the Scala sense.
    * @param b
    */
  def add(b: Byte) = { sum += b }

  /**
    * A method that returns the checksum of the running accumulator sum.
    * @return checksum integer
    */
  def checksum(): Int = ~ (sum & 0xFF) + 1
}

object ChecksumAccumulator {

  private val cache = Map[String, Int]()

  /**
    * Given a string check if the string is in the cache. If so return its checksum. Otherwise
    * compute the checksum and return it, storing it in the cache.
    * @param s
    * @return calcuated checksum
    */
  def calculate(s: String): Int = {
    if (cache.contains(s))
      cache(s)
    else {
      val acc = new ChecksumAccumulator
      for (c <- s)
        acc.add(c.toByte)
      val cs = acc.checksum()
      cache += (s -> cs)
      cs
    }
  }

  def main(args:Array[String]): Unit = {

    for (arg <- args) {
      val cs: Int = calculate(arg)
      println("[Args=" + arg + ": checksum=" + cs + "]")
    }
  }
}
