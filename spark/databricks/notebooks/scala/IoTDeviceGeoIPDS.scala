// Databricks notebook source exported at Thu, 17 Mar 2016 21:58:31 UTC
// MAGIC %md ## How to Process IoT Device Data using Datasets and Dataframes - Part 2

// COMMAND ----------

// MAGIC %md ![Simulation of IoT Connected Devices](https://github.com/dmatrix/examples/blob/master/confluent/images/kafka_pub_sub/kafka_pub_sub.001.jpeg "Simulating IoT Connected Devices")
// MAGIC 
// MAGIC [Part 1] (http://bit.ly/1RhErbF) 

// COMMAND ----------

import main.scala._
import main.scala.DeviceIoTData
import org.apache.spark.{SparkContext, SparkConf}

// COMMAND ----------

// MAGIC %md Use the case class *DeviceIoTData* to convert the JSON device data into a Scala object.
// MAGIC Case class for mapping to a Dataset for the JSON {"device_id": 198164, "device_name": "sensor-pad-198164owomcJZ", "ip": "80.55.20.25", "cca2": "PL", "cca3": "POL", "cn": "Poland", "latitude": 53.080000, "longitude": 18.620000, "scale": "Celius", "temp": 21, "humidity": 65, "battery_level": 8, "c02_level": 1408, "lcd": "red", "timestamp" :1458081226051 }

// COMMAND ----------


//fetch the JSON device information uploaded into the Filestore
val jsonFile = "/FileStore/tables/hll2y8jf1458081917577/iot_devices.json"

//read the json file and create the dataset from the case class
// ds is now a collection of org.apache.spark.sql.Row
val ds = sqlContext.read.json(jsonFile).as[DeviceIoTData]
//show Dataset's first 20 rows
ds.show(5)

// COMMAND ----------

// MAGIC %md Let's iterate over the first 10 entries with the foreach() method

// COMMAND ----------

ds.take(10).foreach(println(_))

// COMMAND ----------

// MAGIC %md Because the Dataset API in 1.6.1 is experimental, and not all relational functionality for writing complext expressions, is still under development, I have elect to convert Dataset to its equivalent Dataframe. Against this dataframe, I can now issue fairly in-depth relation expressions.
// MAGIC 
// MAGIC For all relationa expressions and queries, Tungsten will formulate an optimized logical and physical execute it.
// MAGIC 
// MAGIC For instance, let's first filter the device dataset on temp and humidity predicate, convert the resulting Dataset into its Dataframe, and then select column by names, order by temperature in a descending order, and, finally, display the first 10 items.

// COMMAND ----------

//issue select, map, filter, foreach operations on the datasets, just as you would for Dataframes
// convert the dataset to dataframe and use simple column name select() method.
ds.filter(d => {d.temp > 25 && d.humidity > 70}).toDF().select("device_name", "device_id", "temp", "humidity").orderBy($"temp".desc).show(10)

// COMMAND ----------

// MAGIC %md By contrast, here I don't convert to Dataframe but stick to Dataset APIs for filtering: take(10) returns an Array[DeviceIoTData]; using a foreach() method on the Array collection, I print each item.

// COMMAND ----------

//filter out dataset rows that meet the temperature and humimdity predicate
val dsFilter = ds.filter (d => {d.temp > 30 && d.humidity > 70}).take(10).foreach(println(_))

// COMMAND ----------

// MAGIC %md To illustrate the functional nature of Scala, let's define a function which we can use to log (and alert) battery replacements for devices whose battery levels == 0 

// COMMAND ----------

def logBatteryReplacement(log: java.io.PrintStream = Console.out, row: org.apache.spark.sql.Row): Unit = {
val message = "[***ALERT***: BATTERY NEEDS REPLACEMENT: device_name: %s; device_id: %s ; cca3: %s]" format(row(0), row(1), row(2))
  log.println(message)
}

// COMMAND ----------

// MAGIC %md Check the cluster's stderr log, where these alert messages
// MAGIC are logged.
// MAGIC 
// MAGIC *Note* that I'm converting the Dataset to Dataframe because the select() APIs in 1.6 is still experimental and not fully functitonal.
// MAGIC 
// MAGIC Look at Spark Jobs' DAGs to visualize the stages and where Tungsten comes into play to execute the physical plan.

// COMMAND ----------

//filter dataset rows with battery level == 0 and apply our defined funcion to each element to log an alert.
val dsBatteryZero = ds.filter (d => {d.battery_level == 0}).toDF().select("device_name", "device_id", "cca3")
//apply logBatteryReplacement() method on the new Dataset
dsBatteryZero.foreach(d => logBatteryReplacement(Console.err, d))

// COMMAND ----------

// MAGIC %md Since I'm having loads of fun and feeling adventerous, I'll attempt a more sophisticated relational query expression on the dataset. For instance, apply filter, convert to dataframe, groupBy, and compute average.

// COMMAND ----------

// apply filter, convert to dataframe, groupBy, and compute average
val dsGroupBy = ds.filter (d => {d.temp > 30 && d.humidity > 70}).toDF().groupBy($"cca3").avg("temp")
dsGroupBy.show(10)

// COMMAND ----------

// MAGIC %md Finally, the fun bit. Data without visualization, withouth a narrative arch to infer insights or to see some problems is useless. We desire to make sense of the results.
// MAGIC 
// MAGIC By saving our Dataset, after converting to Dataframe, as a temporary table, I can issue more complext SQL queries against it and visualize the results, using notebook's myriad plotting options.

// COMMAND ----------

ds.toDF().registerTempTable("iot_device_data")

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as devices from iot_device_data group by cca3 order by devices desc limit 1000

// COMMAND ----------

// MAGIC %md Filter on humidity and C02 levels on only devices of that match the pattern *therm%* (in other words only therm-sticks), order them by humitdity in a descending order.

// COMMAND ----------

// MAGIC %sql select cca3, c02_level, device_name, humidity from iot_device_data where humidity > 80 and c02_level > 1400 and device_name like 'therm-stick%' order by humidity, c02_level desc limit 1000

// COMMAND ----------

// MAGIC %md Let's visualize the results as a pie chart and distribution

// COMMAND ----------

// MAGIC %sql select cca3, c02_level from iot_device_data where c02_level > 1400 order by c02_level desc

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as devices from iot_device_data group by cca3 order by devices desc limit 10

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where lcd == 'red' group by cca3 order by device_id desc limit 100

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where battery_level == 0 group by cca3 order by device_id desc limit 100

// COMMAND ----------

// MAGIC %md ##Conclusion

// COMMAND ----------


