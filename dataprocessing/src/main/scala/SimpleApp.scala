// SimpleApp.scala 
import org.apache.spark.SparkContext
import org.apache.spark.SparkContext._
import org.apache.spark.SparkConf

//creating a singleton object
object SimpleApp {
  def main(args: Array[String]) {
     // Should be some file on your system
    val logFile = args[0]
    val conf = new SparkConf().setAppName("Simple Log Application")
    val sc = new SparkContext(conf)
    val logData = sc.textFile(logFile, 3).cache()
    val numEs = logData.filter(line => line.contains("ERROR")).count()
    val numIs = logData.filter(line => line.contains("INFO")).count()
    val numWs = logData.filter(line => line.contains("WARNING")).count()
    println("Lines with ERROR: %s, Lines with INFO: %s, Lines with WARNING: %s".format(numEs, numIs, numWs))
  }
}
