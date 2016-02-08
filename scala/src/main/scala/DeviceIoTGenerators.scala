package main.scala

import java.util.concurrent.CountDownLatch

import scala.collection.mutable

/**
  * Created by jules on 12/29/15.
  */
class DeviceIoTGenerators(range :Range, countDownLatch: CountDownLatch) extends Runnable {

  val this.range = range
  var deviceMap: List[mutable.Map[String, String]] = List()
  var done: Boolean = false
  var thrName = ""
  var mCountDown: CountDownLatch = countDownLatch

  def run: Unit = {

    thrName = Thread.currentThread().getName()
    println("Generating Devices info in " + thrName)
    deviceMap = DeviceProvision.getDeviceBatch(this.range)
    done = true
    countDownLatch.countDown()
  }

  def isDone(): Boolean = return done
  def getDeviceBatches(): List[mutable.Map[String, String]] = return deviceMap
}
