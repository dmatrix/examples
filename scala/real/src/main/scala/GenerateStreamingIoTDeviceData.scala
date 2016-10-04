package main.scala

import java.io.{File, IOException, PrintWriter}

import main.scala.DeviceProvision.{getC02LevelWithTrend, getSignalWithDownTrend, getRandomNumber, getSignal, getTemperatureWithTrend}

import scala.collection.mutable.Map
import scala.io.Source
import scala.util.control.Breaks


/**
  * Created by jules on 9/21/16.
  * /**
  * author: Jules S. Damji
  * date: 9/21/2016
  * This short scala program generates simulated device data information for any IoT devices connected globally. It assusmes that
  * the device can generate events which are collected at a central collection agent that converts them into a JSON document
  * and publishes them onto to publish/subscribe networks such as PubNub or Apache Kafka.
  *
  * The consumers, then, can store them for batch processing logs for visualization and analyses. Alternatively, a Spark realtime application
  * can consume streams of these events as JSON documents and act on it in realtime for alters on high values of individual data points
  * such as temperature, humidity, battery power...etc
  *
  * For visualization and analyses, I use generated datasets from this Scala program in a Databricks Scala Notebooks.\
  * To run this program:
  * scala -cp target/scala-2.10/src-main-scala_2.10-1.0.jar main.scala.GenerateStreamingIoTDeviceData /Users/jules/examples/py/ips/data/country_codes.txt /Users/jules/examples/py/ips/data/ips_info.txt destination_dir number_of_files
  * number_devices_per_file
  */
  */
object GenerateStreamingIoTDeviceData {


    val ccodes = Map[String, String]()
    val lastTrendValues = Map[String, Int]()

    def getDeviceType(id: Int): String = {
      val deviceType = if (id % 2 == 0) {
        "sensor-ipad"
      } else if (id % 3 == 0) {
        "sensor-inest"
      } else if (id % 5 == 0) {
        "sensor-istick"
      } else {
        "sensor-igauge"
      }
      return deviceType
    }

    def toJSonDeviceData(line: String, id: Int, mode: Int): String = {

      var djson = ""
      val tokens = line.split(",")
      val len = tokens.length
      if (len == 11) {
        val ip = tokens(0)
        val cca2 = tokens(1)
        val cn = tokens(2)
        //set cca2 and caa3 to US and USA if a mapping doesn't exit
        var cca3 = "USA"
        if (ccodes.contains(cca2))
          cca3 = ccodes(cca2)
        val deviceType = getDeviceType(id)
        var signal = 0
        if (mode == 0) {
          signal = getSignalWithDownTrend(lastTrendValues("signal"))
        } else {
          signal = getSignalWithDownTrend(0)
        }
        lastTrendValues("signal") = signal
        val battery = if (signal <= 15) getRandomNumber(0, 2) else getRandomNumber(3, 10)
        var c02 = 0
        if (mode == 0) {
          c02 = getC02LevelWithTrend(lastTrendValues("c02"))
        } else  {
          c02 = getC02LevelWithTrend(0)
        }
        //set the max: indicates a huge problem
        if (c02 > 2000) {
          c02 = 1000
        }
        lastTrendValues("c02") = c02
        var temperature = 0
        if (mode == 0) {
          temperature = getTemperatureWithTrend(lastTrendValues("temp"))
        } else {
          temperature = getTemperatureWithTrend(0)
        }
        //set the max: has reached over 100 degree Centrigrade
        if (temperature > 110) {
          temperature = 50
        }
        lastTrendValues("temp") = temperature
        val timestamp: Long = (System.currentTimeMillis / 1000.0).toLong + 60

        djson = "{\"device_id\": %d, " +
            "\"device_type\": \"%s\", " +
            "\"ip\": \"%s\", " +
            "\"cca3\": \"%s\", " +
            "\"cn\": \"%s\", " +
            "\"temp\": %d, " +
            "\"signal\": %d, " +
            "\"battery_level\": %d, " +
            "\"c02_level\": %d, " +
            "\"timestamp\" :%d }" format(id, deviceType, ip, cca3, cn, temperature, signal, battery, c02, timestamp)
        }
        djson
    }

    def main(args: Array[String]): Unit = {

      if (args.length != 6) {
        println("""Need four arguments: <path_to_country_codes.txt> <path_to_ips_info.txt> <output_directory> number_of_files number_of_devices_per_file trend_every_nth_file""")
        System.exit(2)
      }

      val numOfFiles: Int = args(3).toInt
      val numOfDevicesPerFile: Int = args(4).toInt
      val numTrend = args(5).toInt
      val deviceFileNames: String = "devices"
      val deviceFileNameSuffix: String = ".json"
      // set some initail values for trending data
      lastTrendValues("temp") = 35
      lastTrendValues("c02") = 800
      lastTrendValues("signal") = 20

      /**
        * Build the ISO-31661-1 2 letter country code to three letter country codes cca2->caa3 mapping.
        * For example, NO -> NOR, PH -> PHL etc
        */
      for (cline <- Source.fromFile(args(0)).getLines()) {
        val tokens = cline.split(" ")
        if (tokens.length == 2)
          ccodes(tokens(0)) = tokens(1)
      }
      /**
        * Read each line from the ip's info read from http://freegeoip.net/csv/<ip>, parse it, and extract relevant
        * info needed. For example, IP, cca2, country, latitude and longitude etc
        */
      val inner = new Breaks

      var w: PrintWriter = null
      //iterate through number of files to generate
      for (i <-1 to numOfFiles) {
        val fn: String = args(2) + "/" + deviceFileNames + "-" + i + deviceFileNameSuffix
        try {
          w = new PrintWriter(new File(fn))
          var id = 0
          inner.breakable {
              //for each ip address read from the source generate a ddvice IoT data information
              for (ipline <- Source.fromFile(args(1)).getLines()) {
                //generate an increasing/decreassing treand every nth file, specified by the input command line argument
                val json = toJSonDeviceData(ipline, id, i % numTrend)
                if (json.length > 0) {
                  w.write(json)
                  w.write("\n")
                  println(json)
                } else {
                  println("Failed to create JSON device data for " + ipline + " skipping...")
                }
                id += 1
                //we have reached number of devices for this file, break the loop, and go to
                // the next file
                if (id == numOfDevicesPerFile && w != null) {
                  w.close()
                  inner.break()
                }
                //sleep for 2 seconds between each device entry
                Thread.sleep(2000)
              }
            }
          println("Created device file:" + fn + " with " + id + " devices")
        }
        catch {
          case ex: IOException => {
            ex.printStackTrace()
            println("IO Exception")
          }
        } finally {
          if (w != null)
            w.close()
        }
        //sleep for three seconds between each file
        Thread.sleep(3000)
      }
  }
}
