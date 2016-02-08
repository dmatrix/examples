package main.scala

import java.io.{File, PrintWriter}
import java.util.concurrent.CountDownLatch

/**
  * Created by jules on 2/8/16.
  * This short program generates JSON file for each device. It uses DeviceIoTGenerators executor pool of threads to
  * generate large data sets.
  *
  * The dataset generate can be use to demonstrate the usage of and differences between RDDs, Datatframes, and Datasets in
  * Apache Spark
  */
object GenerateIoTDeviceData {

  /**
    * Generate a json string from the map and append to the file
    *
    * @param elem
    * @param w
    */
    def generateJsonFile(elem: scala.collection.mutable.Map[String, String], w:PrintWriter): Unit = {
          val id: Int = elem.get("device_id").get.toInt
          val dev: String = elem.get("device_name").get.toString
          val timestamp: Int = elem.get("timestamp").get.toInt
          val temp: Int = elem.get("temp").get.toInt
          val scale: String = elem.get("scale").get.toString
          val xcoor: Int = elem.get("lat").get.toInt
          val ycoor: Int = elem.get("long").get.toInt
          val zip: Int = elem.get("zipcode").get.toInt
          val humidity: Int = elem.get("humidity").get.toInt
          val djson = "{\"device_id\": %d, \"device_name\": \"%s\", \"timestamp\":%d, \"temp\": %d, \"scale\": \"Celius\", \"lat\": %d, \"long\": %d, \"zipcode\": %d, \"humidity\": %d}" format(id, dev, timestamp, temp, xcoor, ycoor, zip, humidity)

          w.write(djson)
          w.write("\n")
    }

    def main(args:Array[String]): Unit = {

      val nDevices = args(0).toInt
      val latch: CountDownLatch = new CountDownLatch(1)
      // for easy of creating equal batches, let's force the device number to be a multiple of three
      if (nDevices % 3 != 0) {
        println("Number of devices must be multiple of 3.")
        System.exit(1)
      }

      val range = 1 until nDevices
      val dgen = new DeviceIoTGenerators(range,latch)
      val thrd = new Thread(dgen)
      thrd.start()

      try {
        println("Generating " + nDevices + " Devices' data")
        print("Awaiting for the Device Generator thread to end...")
        latch.await()
        println("Device Generators Thread ends\n")
      }
      catch {
        case e: InterruptedException => {
          e.printStackTrace
          System.exit(1)
        }
      }
      val batches = dgen.getDeviceBatches()

      val writer = new PrintWriter(new File("/Users/jules/data/iot/IoTDevices.json"))
      batches.foreach(e => generateJsonFile(e, writer))

      writer.close();
      println("Finished...")
  }

}
