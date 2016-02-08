package main.scala
import scala.collection.mutable.Map

/**
	* This example illustrates how to use a Singleton scala object with the same names as the class object, within the
	* same file. Note that as Singleton object, it can be use outside this class as well.
	*/
object DeviceProvision {

	val author = "Jules S. Damji"
	val what = "Learning Scala!"
	val choice = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
	val rnd = new util.Random()

	/**
		* Get a randome from within the specifed range
		* @param from
		* @param to
		* @return generated number
    */
	def getRandomNumber(from: Int, to: Int): Int = {
		return from + rnd.nextInt(Math.abs(to - from))
	}

	/**
		* Generate a random zipcode between two legal ranges
		* @return generated zip code
    */
	def getZipCode(): Int = {
		return getRandomNumber(94538, 97107)
	}

	/**
		* Generate a random X coordinate
		* @return generated x coordinate
    */
	def getX(): Int = {
		val x: Int = getRandomNumber(10, 100)
		return x
	}

	/**
		* Generate a random Y coordinate
		* @return generated Y coordinate
    */
	def getY(): Int = {
		val y: Int = getRandomNumber(10, 100)
		return y
	}

	/**
		* Generate random humidity between two numbers: min and max
		* @return generated humidity
    */
	def getHumidity(): Int = {
		return getRandomNumber(25, 100)
	}

	/**
		* Generate a random temperature between two min and max
		* @return generated temperature
    */
	def getTemperature(): Int = {
		return getRandomNumber(10, 35)
	}

	/**
		* Generate a randome string from with an alphate range between min and max len
		* @param minLen
		* @param maxLen
		* @return
    */
	def getRandomString(minLen: Int = 5, maxLen: Int = 10): String = {
		val len = rnd.nextInt(1 + maxLen - minLen) + minLen
		val sb = new StringBuilder(len)
		for (_ <- 0 until len) {
			sb.append(choice.charAt(rnd.nextInt(choice.length())))
		}
		return sb.toString
	}

	/**
		* Create a Map of device information
		* @param dev device name
		* @param id device id
		* @return Map[String, String]
    */
	def createDeviceData(dev: String, id: Int): scala.collection.mutable.Map[String, String] = {
		val dmap = scala.collection.mutable.Map[String, String]()
		val temp = getTemperature()
		val humidity = getHumidity()
		val zip = getZipCode()
		val xcoor = getX();
		val ycoor = getY();
		val timestamp: Long = System.currentTimeMillis / 1000
		dmap.put("device_name", dev)
		dmap.put("device_id", id.toString)
		dmap.put("timestamp", timestamp.toString)
		dmap.put("temp", temp.toString)
		dmap.put("scale", "Celcius")
		dmap.put("lat", xcoor.toString)
		dmap.put("long", xcoor.toString)
		dmap.put("zipcode", zip.toString)
		dmap.put("humidity", humidity.toString)
		//val djson = "{\"device_id\": %d, \"device_name\": \"%s\", \"timestamp\":%d, \"temp\": %d, \"scale\": \"Celius\", \"lat\": %d, \"long\": %d, \"zipcode\": %d, \"humidity\": %d}" format(id, dev, timestamp, temp, xcoor, ycoor, zip, humidity)
		//println(djson)
		return dmap
	}

	/**
		* Create a collection, List, of Map[String, String] for range of device id
		* @param range of device ids
		* @return List[Map[String, String]]
    */
	def getDeviceBatch(range: Range): List[Map[String, String]] = {
		var batch: List[Map[String, String]] = List()
		var id: Int = range.start
		var device: String = ""
		for (id <- range.start to range.end) {
			if (id % 2 == 0) {
				device = "sensor-pad-" + id.toString + getRandomString()
			} else if (id % 3 == 0) {
				device = "device-mac-" + id.toString + getRandomString()
			} else if (id % 5 == 0) {
				device = "therm-stick-" + id.toString + getRandomString()
			} else {
				device = "meter-gauge-" + id.toString + getRandomString()
			}
			Thread.sleep(10)
			val djson = createDeviceData(device, id)
			batch = batch.::(djson)
		}
		return batch.reverse
	}

	/**
		* Generate a luv message :)
		* @param message
    */
	def myPrint(message: String): Unit = {
		val luv = "...And Luving it!"
		print(message)
		println(author + " is " + what + luv)
	}
}

/**
	*
	* @param number of devices to generate
  */
	class DeviceProvision (number: Int) {
		private val devicesNumber = number

		def main (args: Array[String]): Unit = {

			DeviceProvision.myPrint("Hello World! ")
			val devices = new DeviceProvision(25)
			val range = 0 until devices.devicesNumber
			val batches = DeviceProvision.getDeviceBatch(range)
			batches.foreach(m => println(m.toString))
		}
}