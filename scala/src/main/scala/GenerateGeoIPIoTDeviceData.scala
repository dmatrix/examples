package main.scala

import java.io.{IOException, File, PrintWriter}

import scala.collection.mutable.Map
import scala.io.Source
import DeviceProvision.{getHumidity, getRandomString, getTemperature, getRandomNumber}

/**
  * author: Jules S. Damji
  * date: 3/12/2016
  * This short scala program generates simulated device data information for any IoT devices connected globally. It assusmes that
  * the device can generate events which are collected at a central collection agent that converts them into a JSON document
  * and publishes them onto to publish/subscribe networks such as PubNub or Apache Kafka.
  *
  * The consumers, then, can store them for batch processing logs for visualization and analyses. Alternatively, a Spark realtime application
  * can consume streams of these events as JSON documents and act on it in realtime for alters on high values of individual data points
  * such as temperature, humidity, battery power...etc
  *
  * For visualization and analyses, I use generated datasets from this Scala program in a Databricks Scala Notebooks.
  */
object GenerateGeoIPIoTDeviceData {

  val ccodes = Map[String, String] ()
  val timeDelta = 150000

  def getDeviceName(id: Int): String = {
    var device: String = ""
      if (id % 2 == 0) {
        device = "sensor-pad-" + id.toString + getRandomString()
      } else if (id % 3 == 0) {
        device = "device-mac-" + id.toString + getRandomString()
      } else if (id % 5 == 0) {
        device = "therm-stick-" + id.toString + getRandomString()
      } else {
        device = "meter-gauge-" + id.toString + getRandomString()
      }
      return device
  }

  def toJSonDeviceData(line: String, id: Int): String = {

    var djson = ""
    val tokens = line.split(",")
    val len = tokens.length
    if (len == 11) {
      val ip = tokens(0)
      var cca2 = tokens(1)
      val cn = tokens(2)
      val latitude = tokens(len - 3).toDouble
      val longitude = tokens(len - 2).toDouble
      //set cca2 and caa3 to US and USA if a mapping doesn't exit
      var cca3 = "USA"
      if (ccodes.contains(cca2))
        cca3 = ccodes(cca2)
      else
        cca2 = "US"
      val deviceName = getDeviceName(id)
      val temperature = getTemperature()
      val humidity = getHumidity()
      val battery  = getRandomNumber(0, 10)
      val timestamp: Long = System.currentTimeMillis + timeDelta

      djson = "{\"device_id\": %d, " +
        "\"device_name\": \"%s\", " +
        "\"ip\": \"%s\", " +
        "\"cca2\": \"%s\", " +
        "\"cca3\": \"%s\", " +
        "\"cn\": \"%s\", " +
        "\"latitude\": %f, " +
        "\"longitude\": %f, " +
        "\"scale\": \"Celius\", " +
        "\"temp\": %d, " +
        "\"humidity\": %d, " +
        "\"battery_level\": %d, " +
        "\"timestamp\" :%d }" format(id, deviceName, ip, cca2, cca3, cn, latitude, longitude, temperature, humidity, battery, timestamp)
    }
    djson
  }

  def main(args:Array[String]): Unit = {

      if (args.length != 3) {
        println("Need two files: <country_codes.txt> <ips_info.txt> <ouput_path_to_json_file>")
        System.exit(2)
      }

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
    var id = 0
    var w: PrintWriter = null
    try {
      w = new PrintWriter(new File(args(2)))
      for (ipline <- Source.fromFile(args(1)).getLines()) {
        id += 1
        val json = toJSonDeviceData(ipline, id)
        if (json.length > 0) {
          println(json)
          w.write(json)
          w.write("\n")
        } else {
          println("Failed to create JSON device data for " + ipline + " skipping...")
        }
      }
    } catch {
      case ex: IOException => {
        ex.printStackTrace()
        println("IO Exception")
      }
    } finally  {
      if (id > 0)
        println("JSON device file " + args(2) + " created with " + (id) + " devices.")
      if (w != null)
        w.close()
    }
  }
}
