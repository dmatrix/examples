// Databricks notebook source exported at Fri, 18 Mar 2016 02:07:03 UTC
// MAGIC %md ## How to Process IoT Device JSON Datasets using Datasets and Dataframes - Part 2

// COMMAND ----------

// MAGIC %md As in the [Part 1](http://bit.ly/1RhErbF), this notebook demonstrates the ease and simplicity of using Spark on the Databricks Cloud, without need to provisiom nodes, without need to manage clusters; all is done for you in this free [Databricks Community Edition](http://go.databricks.com/databricks-community-edition-beta-waitlist).
// MAGIC 
// MAGIC With the introduction of [Dataframes](http://spark.apache.org/docs/latest/sql-programming-guide.html#dataframes) in Apache Spark 1.3 and [Datasets](https://databricks.com/blog/2016/01/04/introducing-spark-datasets.html) preview in 1.6, this notebook uses its APIs to show how you can quickly process structued data (JSON) with an inherent schema, intuitively compose relational queries using APIs, and, finally, against a temporary table, issue SQL like queries. By using notebook's myriad plotting options, the results from the SQL queries can be visualized for presenation and narration. Even better, these plots can be added to your dashboard.
// MAGIC 
// MAGIC In this second part, I have augmented the device dataset to include additional attributes, such as GeoIP locations, an idea borrowed from [AdTech Sample Notebook](https://cdn2.hubspot.net/hubfs/438089/notebooks/Samples/Miscellaneous/AdTech_Sample_Notebook_Part_1.html), as well as additional device attributes on which we can log alerts. Unlike the dataset size in [Part 1](http://bit.ly/1RhErbF), I uploaded close to 200K devices, curtailing from 2M entries to a smaller dataset.
// MAGIC 
// MAGIC Again, all code is availabe on my github:
// MAGIC * [Python Scripts](https://github.com/dmatrix/examples/tree/master/py/ips)
// MAGIC * [Scala Libraries](https://github.com/dmatrix/examples/tree/master/scala/src/main/scala)
// MAGIC * [Scala Notebooks](https://github.com/dmatrix/examples/tree/master/spark/databricks/notebooks/scala)
// MAGIC 
// MAGIC Beside importing this notebook into your Databricks Cloud, you can also watch a [screencast]()

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
// MAGIC For all relational expressions, the [Catalyst](https://databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html) will formulate an optimized logical and physical plan for execution, and [Tungsten](https://databricks.com/blog/2015/04/28/project-tungsten-bringing-spark-closer-to-bare-metal.html) engine will efficiently execute the optimized code. For our *DeviceIoTData*, it will use its standard encoders to optimize its binary internal representation, hence decrease the size of generate code, minimize the bytes transfered over the networks between nodes, and execute faster.
// MAGIC 
// MAGIC For instance, let's first filter the device dataset on *temp* and *humidity* attributes with a predicate, convert the resulting Dataset into its Dataframe, and then select column by names, order by temperature in a descending order, and, finally, display the first 10 items.

// COMMAND ----------

//issue select, map, filter, foreach operations on the datasets, just as you would for Dataframes
// convert the dataset to dataframe and use simple column name select() method.
val dsTemps = ds.filter(d => {d.temp > 30 && d.humidity > 70}).toDF().select("device_name", "device_id", "temp", "humidity").orderBy($"temp".desc).show(10)


// COMMAND ----------

// MAGIC %md By contrast, in the next cell, I don't convert to Dataframe but use Dataset APIs for filtering: take(10) returns an Array[DeviceIoTData]; using a foreach() method on the Array collection, I print each item.

// COMMAND ----------

//filter out dataset rows that meet the temperature and humimdity predicate
val dsFilter = ds.filter (d => {d.temp > 30 && d.humidity > 70}).take(10).foreach(println(_))

// COMMAND ----------

// MAGIC %md To illustrate the functional nature of Scala, let's define a function which we can use to log (and alert) battery replacements for devices whose battery levels == 0 

// COMMAND ----------

def logAlerts(log: java.io.PrintStream = Console.out, row: org.apache.spark.sql.Row, alert: String): Unit = {
val message = "[***ALERT***: %s : device_name: %s; device_id: %s ; cca3: %s]" format(alert, row(0), row(1), row(2))
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
val dsBatteryZero = ds.filter(d => {d.battery_level == 0}).toDF().select("device_name", "device_id", "cca3").foreach(d => logAlerts(Console.err, d, "REPLACE DEVICE BATTERY"))

// COMMAND ----------

// MAGIC %md Again, check the cluser logs for C02 alerts

// COMMAND ----------

// filter datasets with dangerous levels of C02 and apply our defined function to each element to log an alert.
val dsHighC02Levels = ds.filter(d => {d.c02_level > 1400 && d.lcd == "red"}).toDF().select("device_name", "device_id", "cca3").foreach(d => logAlerts(Console.err, d, "DEVICE DETECTS HIGH LEVELS OF C02 LEVELS"))

// COMMAND ----------

// MAGIC %md Since I'm having loads of fun and feeling adventurous, I'll attempt a more sophisticated relational query expression on the dataset. For instance, string together a filter, convert to dataframe, groupBy, and compute average.

// COMMAND ----------

// apply filter, convert to dataframe, groupBy, and compute average
val dsGroupBy = ds.filter ( d => {d.temp > 30 && d.humidity > 70} ).toDF().groupBy($"cca3").avg("temp")
dsGroupBy.show(10)

// COMMAND ----------

// MAGIC %md **Finally, the fun bit!**
// MAGIC 
// MAGIC Data without visualization, withouth a narrative arc, to infer insights or to see a trend is useless. We always desire to make sense of the results.
// MAGIC 
// MAGIC By saving our Dataset, after converting to Dataframe, as a temporary table, I can issue complex SQL queries against it and visualize the results, using notebook's myriad plotting options.

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


