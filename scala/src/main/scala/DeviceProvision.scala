import util.Random._
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

	def getRandomNumber(from: Int, to: Int): Int = {
		return from + rnd.nextInt(Math.abs(to - from))
	}

	def getZipCode(): Int = {
		return getRandomNumber(94538, 97107)
	}

	def getCoordinates(): (Int, Int) = {
		val x: Int = getRandomNumber(10, 100)
		val y: Int = getRandomNumber(10, 100)
		return Tuple2(x, y)
	}

	def getHumidity(): Int = {
		return getRandomNumber(25, 100)
	}

	def getTemperature(): Int = {
		return getRandomNumber(10, 35)
	}

	def getRandomString(minLen: Int = 5, maxLen: Int = 10): String = {
		val len = rnd.nextInt(1 + maxLen - minLen) + minLen
		val sb = new StringBuilder(len)
		for (_ <- 0 until len) {
			sb.append(choice.charAt(rnd.nextInt(choice.length())))
		}
		return sb.toString
	}

	def createDeviceData(dev: String, id: Int): scala.collection.mutable.Map[String, Any] = {
		var dmap = scala.collection.mutable.Map[String, Any]()
		val temp = getTemperature()
		val humidity = getHumidity()
		val coord = getCoordinates()
		val zip = getZipCode()
		// create json of the format:
		// {'device_id': id, 'device_name': d, 'timestamp': ts, 'temp': temp, 'scale': 'Celius', "lat": x, "long": y, 'zipcode': zipcode, 'humidity': humidity}
		val timestamp: Long = System.currentTimeMillis / 1000
		dmap.put("device_name", dev)
		dmap.put("device_id", id)
		dmap.put("timestamp", timestamp)
		dmap.put("temp", temp)
		dmap.put("scale", "Celcius")
		dmap.put("lat", coord._1)
		dmap.put("long", coord._2)
		dmap.put("zipcode", zip)
		dmap.put("humidity", humidity)
		//val djson = "{\"device_id\": %d, \"device_name\": \"%s\", \"timestamp\":%d, \"temp\": %d, \"scale\": \"Celius\", \"lat\": %d, \"long\": %d, \"zipcode\": %d, \"humidity\": %d}" format(id, dev, timestamp, temp, coord._1, coord._2, zip, humidity)
		return dmap
	}


	def getDeviceBatch(size: Int): List[Map[String, Any]] = {
		var batch: List[Map[String, Any]] = List()
		var id: Int = 0
		var device: String = ""
		for (id <- 1 to size) {
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
			val batches = DeviceProvision.getDeviceBatch(devices.devicesNumber)
			batches.foreach(m => println(m.toString))
		}
}