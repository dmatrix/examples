package main.scala

import scala.collection.mutable

/**
  * Created by jules on 12/29/15.
  */
class DeviceIoTGenerators(range :Range) extends Runnable{

  val this.range = range
  var deviceMap: List[mutable.Map[String, String]] = List()
  var done: Boolean = false
  var thrName = ""

  def run: Unit = {

    thrName = Thread.currentThread().getName()
    println("Generating Devices info in:  " + thrName)
    deviceMap = DeviceProvision.getDeviceBatch(this.range)
    done = true
  }

  def isDone(): Boolean = return done
  def getDeviceBatches(): List[mutable.Map[String, String]] = return deviceMap
}
