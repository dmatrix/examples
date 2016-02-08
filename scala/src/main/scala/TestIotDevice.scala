package main.scala

import java.util.concurrent.CountDownLatch

import scala.collection.mutable.Map
import scala.util.matching.Regex

/**
  * Simple illustration of of using Maps, Singleton Objects, and threads defined in a package as well as usage of
  * collection filters and foreach methods on collections.
  */

object TestIotDevice {
  /**
    * Filter each device map where the k/v satisfy a predicate
    *
    * @param elem Map[String, String]
    * @param k key in the Map
    * @param v value to satisfy
    * @return return true or false
    */
  def filterByKey(elem: scala.collection.mutable.Map[String, String], k:String, v: Int): Boolean = {
    val value:Int = elem.get(k).get.toInt
    return (value >= v)
  }

  /**
    * Filter each device map where the device_name key satisfies the regex pattern
    *
    * @param elem Map[String, String]
    * @param pattern Regex that matches the device pattern
    * @return true or false
    */
  def filterByDevicePattern(elem: scala.collection.mutable.Map[String, String], pattern:scala.util.matching.Regex): Boolean = {
    val value:String = elem.get("device_name").get
    return (pattern.findFirstIn(value) != None)

  }

  /**
    *  Main routine
    *
    * @param args command line arguments
    */
  def main(args: Array[String]) {
    //TODO use parse command line arguments with options
    //number of devices
    val nDevices: Int = args(0).toInt
    // filter for humidity
    val value1 = args(1).toInt
    // filter for temperature
    val value2 = args(2).toInt
    //string pattern by device, eg. mac-, therm, sensor, etc
    val pattern = args(3)
    val latch: CountDownLatch = new CountDownLatch(1)
    //Use singleton object's method
    DeviceProvision.myPrint("Hello World! ")
    // Use the Scala thread to create devices batches, for a range, as a List of Maps[String, String] and print them out
    val range = 1 until nDevices
    val dgen = new DeviceIoTGenerators(range, latch)
    val thrd = new Thread(dgen).start()
    latch.await()
    //Thread.sleep(1000)
    val batches = dgen.getDeviceBatches()
    batches.foreach(println(_))
    println()
    // Use filter methods to create collections where one of the key's value on each K/V in the list of maps satisfies
    // the predicate. Print the newly filtered batches
    // Filter the first batch on humidity k/v
    val filteredBatches1: List[Map[String, String]] = batches.filter(filterByKey(_, "humidity", value1))
    printf("%d Devices found where humidity is greater than or equal to %d\n", filteredBatches1.length, value1)
    filteredBatches1.foreach(println(_))
    // Filter the second batch on temperatue k/v
    val filteredBatches2: List[Map[String, String]] = batches.filter(filterByKey(_, "temp", value2))
    println()
    printf("%d Devices found where temperature is greater than or equal to %d\n", filteredBatches2.length, value2)
    filteredBatches2.foreach(println(_))
    val filteredBatches3: List[Map[String, String]] = batches.filter(filterByDevicePattern(_, new Regex("^"+pattern)))
    println()
    //Filter by device that matches a regx pattern
    printf("%d Devices found where they match a pattern %s\n", filteredBatches3.length, "^"+ pattern)
    filteredBatches3.foreach(println(_))
  }
}