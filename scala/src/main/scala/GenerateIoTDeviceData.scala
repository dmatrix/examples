package main.scala

import java.io.{IOException, FileNotFoundException, File, PrintWriter}
import java.util.concurrent.{Executors, CountDownLatch}

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
    * @param listElem
    * @param w
    */
    def generateJsonFile(listElem: List[scala.collection.mutable.Map[String, String]], w:PrintWriter): Unit = {
        listElem.foreach(elem => ( {
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
          w.write("\n") }
          ) )
    }

    def main(args:Array[String]): Unit = {

      if (args.length != 2 ) {
        println("Usage: number_of_devices <output_path_filename.json>")
        System.exit(1)
      }
      val nDevices = args(0).toInt
      val jsonFile = args(1)
      // for easy of creating equal batches, let's force the device number to be a multiple of three
      if (nDevices % 3 != 0) {
        println("Number of devices must be multiple of 3.")
        System.exit(1)
      }

      //create a pool of three threads, assuming we have three cores
      val cores = 3
      val pool = Executors.newFixedThreadPool(cores)
      val multiple = nDevices / 3
      var devGenerators: List[DeviceIoTGenerators] = List()
      val latch: CountDownLatch = new CountDownLatch(cores)
      //create list of three DeviceGenerator Runnable(s), each with its begin..end range of device numbers and the latch
      //associated with it for counting down.
      devGenerators = devGenerators.::(new DeviceIoTGenerators(1 until multiple, latch))
      devGenerators = devGenerators.::(new DeviceIoTGenerators(multiple + 1 until 2 * multiple, latch))
      devGenerators = devGenerators.::(new DeviceIoTGenerators((2 * multiple) + 1 until 3 * multiple, latch))
      // Using foreach method on the list, submit each runnable to the executor service thread pool
      println("Generating " + nDevices + " Devices' data in " + jsonFile)
      println("Launching 3 threads and waiting for them to end...")
      devGenerators.foreach(pool.submit(_))
      // Using a LatchCountDown mechanism, let each Runnable finish in the executor pool.
      try {
        latch.await()
        println("All Device Generators Threads ended\n")
      } catch {
        case e: InterruptedException => {
          e.printStackTrace
          System.exit(1)
        }
      }
      // reverse the order of the List, since in Scala, for efficiency, Lists are appended to the front.
      devGenerators = devGenerators.reverse
      try {
        val writer = new PrintWriter(new File(jsonFile))
        devGenerators.foreach(e => generateJsonFile(e.getDeviceBatches(), writer))

        writer.close();
        println("Finished! File " + jsonFile + " created.")
      } catch {
        case ex: IOException => {
          println("IO Exception")
        }
      }
    }
}
