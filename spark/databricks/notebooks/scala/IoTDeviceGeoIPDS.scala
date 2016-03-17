// Databricks notebook source exported at Thu, 17 Mar 2016 00:39:53 UTC
import main.scala._
import main.scala.DeviceIoTData
import org.apache.spark.{SparkContext, SparkConf}


// COMMAND ----------

// MAGIC %md Use the case class so that we can use a Dataset and Dataframe interchangably
// MAGIC Case class for mapping to a Dataset for the JSON {"device_id": 198164, "device_name": "sensor-pad-198164owomcJZ", "ip": "80.55.20.25", "cca2": "PL", "cca3": "POL", "cn": "Poland", "latitude": 53.080000, "longitude": 18.620000, "scale": "Celius", "temp": 21, "humidity": 65, "battery_level": 8, "c02_level": 1408, "lcd": "red", "timestamp" :1458081226051 }

// COMMAND ----------


//fetch the JSON device information uploaded into the Filestore
val jsonFile = "/FileStore/tables/hll2y8jf1458081917577/iot_devices.json"

//read the json file and create the dataset using the case class
val ds = sqlContext.read.json(jsonFile).as[DeviceIoTData]

//show Dataset's first 20 rows
ds.show(5)

// COMMAND ----------

//issue select, map, filter, foreach operations on the datasets, just as you would for Dataframes
// convert the dataset to dataframe and use simple column name select() method.
ds.toDF().select("device_name", "device_id", "temp", "humidity").orderBy($"temp").show(10)

// COMMAND ----------

//filter out dataset rows that meet the temperature and humimdity predicate
val dsFilter = ds.filter (d => {d.temp > 30 && d.humidity > 70})
dsFilter.show(10)

// COMMAND ----------

// MAGIC %md Define a function we can use to log battery replacements for devices.

// COMMAND ----------

def logBatteryReplacement(log: java.io.PrintStream = Console.out, row: org.apache.spark.sql.Row): Unit = {
val message = "[***ALERT***: BATTERY NEEDS REPLACEMENT: device_name: %s; device_id: %s ; cca3: %s]" format(row(0), row(1), row(2))
  log.println(message)
}

// COMMAND ----------

//filter dataset rows with battery level == 0 and apply our defined funcion to each element to log an alert.
val dsBatteryZero = ds.filter (d => {d.battery_level == 0}).toDF().select("device_name", "device_id", "cca3")
dsBatteryZero.foreach(d => logBatteryReplacement(Console.err, d))

// COMMAND ----------

// MAGIC %md Let's appy a more sophisticated query on the dataset

// COMMAND ----------

// apply filter, convert to datafream, groupBy, and compute average
val dsGroupBy = ds.filter (d => {d.temp > 30 && d.humidity > 70}).toDF().groupBy($"cca3").avg("temp")
dsGroupBy.show(10)

// COMMAND ----------

// MAGIC %md Save the Dataset as a temporary table and issue SQL queires against it.

// COMMAND ----------

ds.toDF().registerTempTable("iot_device_data")

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data group by cca3 order by device_id desc limit 1000

// COMMAND ----------

// MAGIC %sql select cca3, c02_level, device_name, humidity from iot_device_data where humidity > 80 and c02_level > 1400 and device_name like 'therm%' order by humidity, c02_level desc limit 1000

// COMMAND ----------

// MAGIC %sql select cca3, c02_level from iot_device_data where c02_level > 1400 order by c02_level desc

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as devices from iot_device_data group by cca3 order by devices desc limit 10

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where lcd == 'red' group by cca3 order by device_id desc limit 100

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where battery_level == 0 group by cca3 order by device_id desc limit 100

// COMMAND ----------


