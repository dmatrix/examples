// Databricks notebook source exported at Mon, 30 Nov 2015 22:33:39 UTC
// MAGIC %md In this equivalent Scala notebook version of the Python version, I mimic identical provisioning of devices, pretending to be sensors emitting data at periodic intervals. With the dataset generated as JSON objects, I create dataframes, save as a temporary table, and issue query commands. Also, with dataframes, I can visualize them in myriad plots, using fields for different kinds of graphs. The reason for the identical notebook was for me to learn Scala.
// MAGIC 
// MAGIC While this short example illustrates how to create JSON objects (as fake device information using random generated strings as device names), create an RDD schema as a dataframe, and register it as a temporary table, it demostrates Spark API's high-level abstraction?dataframes on top of RDD?allowing developers to manipulate and process data with relative easy and with a familiar query langauge. 
// MAGIC 
// MAGIC But most importantly, with such brevity and simplicity!
// MAGIC 
// MAGIC That such is a draw of Spark API is no surprise. Try it out for yourself!

// COMMAND ----------

import util.Random 
/**
 * Some variables needed through out the scope of this program
 */
val author = "Jules S. Damji"
val what   = "Learning Scala!"
val choice = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
val rnd = new Random()

/** Print out any message string
*/
def myPrint(message: String) : Unit = {
  val luv = "...And Luving it!"
  print(message)
  println(author + " is " + what + luv)
}
/**
 * Generate a random number between two integer numbers
 */
def getRandomNumber(from:Int, to:Int) : Int = {
	return from + rnd.nextInt(Math.abs(to - from))
}
/**
 * Generate zipcode between two valid zipcodes. Restrict to CA codes
 */
def getZipCode(): Int = {
		return getRandomNumber(90001,96162)
}

/**
 * Generate random coordinates
 */
def getCoordinates(): (Int, Int) = {
	val x:Int = getRandomNumber(10, 100)
	val y:Int = getRandomNumber(10, 100)
	return Tuple2 (x, y)
}

/**
 * Generate humidity
 */
def getHumidity(): Int = {
	return getRandomNumber(25, 100)
}
/**
 * Generate temperature
 */
def getTemperature(): Int = {
	return getRandomNumber(10, 35)
}

/**
 * Use random strings to append to a device name
 */
def getRandomString(minLen:Int=5, maxLen:Int=10): String = {
    val len = rnd.nextInt(1 + maxLen - minLen) + minLen
    val sb = new StringBuilder(len)
    for (_ <- 0 until len) {
        sb.append(choice.charAt(rnd.nextInt(choice.length())))
    }
    return sb.toString
}
/**
 * get an IP
 */
def getIPAddr : String = {
  val tuple4 = Tuple4(getRandomNumber(60, 209), getRandomNumber(1, 127), getRandomNumber(1, 127), getRandomNumber(1, 127))
  return tuple4.productIterator.map {_.toString} mkString (".")
}
/**
 * create Device JSON string
 */
def createDeviceData(dev: String, id:Int): String = {
  val temp = getTemperature()
  val humidity = getHumidity()
  val coord    = getCoordinates()
  val zip = getZipCode()
  val ip = getIPAddr
  // create json of the format:
  // {'device_id': id, 'device_name': d, 'ip': ipaddr, timestamp': ts, 'temp': temp, 'scale': 'Celius', "lat": x, "long": y, 'zipcode': zipcode, 'humidity': humidity}
  val timestamp: Long = System.currentTimeMillis / 1000
  val djson = "{\"device_id\": %d, \"device_name\": \"%s\", \"ip\": \"%s\", \"timestamp\":%d, \"temp\": %d, \"scale\": \"Celius\", \"lat\": %d, \"long\": %d, \"zipcode\": %d, \"humidity\": %d}" format(id, dev, ip, timestamp, temp, coord._1, coord._2, zip, humidity)
    return djson
}
/**
 * generate a batch of 'size' devices as JSON objects
 */
def getDeviceBatch(size: Int) : List[String] = {
	var batch: List[String] = List()
	var id: Int = 0
	var device: String = ""
	for (id <-1 to size ) {
		if (id % 2 == 0) {
			device = "sensor-pad-" + id.toString + getRandomString()
		} else if (id % 3 == 0) {
			device = "device-mac-" + id.toString + getRandomString()
		} else if (id % 5 == 0) {
			device = "therm-stick-" + id.toString + getRandomString()
		} else {
			device = "meter-gauge-" + id.toString + getRandomString()
		}
		Thread.sleep(250)
		val djson = createDeviceData(device, id)
		batch = batch.::(djson)
	}
	return batch.reverse
}
// get the 1000 devices
var batches = getDeviceBatch(1000)

// COMMAND ----------

myPrint("Hello World! ")

// COMMAND ----------

// MAGIC %md Create 10 parallel RDDs

// COMMAND ----------


val devicesRDD = sc.parallelize(batches, 10)
devicesRDD.take(5)

// COMMAND ----------

// MAGIC %md create a Dataframe by reading the JsonRDDs. Note how the schema is inferred from the JSON object

// COMMAND ----------

val df = sqlContext.read.json(devicesRDD)
df.printSchema()

// COMMAND ----------

// MAGIC %md Display the the dataframe and use some plotting options

// COMMAND ----------

display(df)

// COMMAND ----------

// MAGIC %md Register as a temporary table against which you can issue SQL commands

// COMMAND ----------

df.registerTempTable("deviceTables")

// COMMAND ----------

// MAGIC %md Try some basic SQL queries

// COMMAND ----------

// MAGIC %sql select count(*) from deviceTables

// COMMAND ----------

// MAGIC %sql select device_id, device_name, ip, humidity, temp from deviceTables where temp > 20 and humidity < 50

// COMMAND ----------

// MAGIC %sql select device_id, device_name, humidity, temp, zipcode, timestamp from deviceTables where zipcode > 91000

// COMMAND ----------

// MAGIC %sql select device_id, device_name, humidity, zipcode, temp from deviceTables where device_name like 'therm%' and humidity < 50

// COMMAND ----------

// MAGIC %sql select device_id, device_name, humidity, zipcode, temp from deviceTables where device_name like 'therm%' and humidity > 50

// COMMAND ----------


