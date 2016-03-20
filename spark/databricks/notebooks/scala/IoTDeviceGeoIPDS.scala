// Databricks notebook source exported at Sun, 20 Mar 2016 17:29:46 UTC
// MAGIC %md ## How to Process IoT Device JSON Data Using Dataset and DataFrames - Part 2

// COMMAND ----------

// MAGIC %md #### Pursuing simplicity and ubiquity
// MAGIC "Spark is a developer's delight" is a common refrain heard among Spark's developer community. Since its inception the vision?the guiding North Star?to make big data processing simple at scale has not faded. In fact, each subsequent release of Apache Spark, from 1.0 to 1.6, seems to have adhered to that guiding principle?in its architecture, in its consistency and parity of APIs across programming languages, and in its unification of major library components built atop the Spark core that can handle a shared data abstraction such as RDDs, DataFrames or Datasets.
// MAGIC 
// MAGIC Since Spark's early days, its creators embraced Alan Kay's principle that "simple things should be simple, complex things possible." And they articulated and reiterated that commitment to the community at the [Spark Summit NY, 2016](https://spark-summit.org/east-2016/schedule/): the keynotes and the release road map attest to that vision of [simplicity](https://www.youtube.com/watch?v=ZFBgY0PwUeY&feature=youtu.be) and [accessibility](https://www.youtube.com/watch?v=BPotQuqFnyw&feature=youtu.be) to the community so everyone can get the "feel of Spark." 
// MAGIC 
// MAGIC And for us to get that "feel of Spark," as in [Part 1](http://bit.ly/1RhErbF), this notebook demonstrates the ease and simplicity with which you can use Spark on the Databricks Cloud, without need to provision nodes, without need to manage clusters; all done for you, all free with [Databricks Community Edition](http://go.databricks.com/databricks-community-edition-beta-waitlist).
// MAGIC 
// MAGIC With [DataFrames](http://spark.apache.org/docs/latest/sql-programming-guide.html#dataframes) (introduced in 1.3) and [Datasets](https://databricks.com/blog/2016/01/04/introducing-spark-datasets.html) (previewed in 1.6), in this notebook I use both sets of APIs to show how you can quickly process structured data (JSON) with an inherent and inferred schema, intuitively compose relational queries, and finally issue [Spark SQL](http://spark.apache.org/docs/latest/sql-programming-guide.html) queries against a table. By using notebook's myriad plotting options, you can visualize results for presenation and narration. Even better, you can save these plots as dashboards.
// MAGIC 
// MAGIC In this second part, I have augmented the device dataset to include additional attributes, such as GeoIP locations, an idea borrowed from [AdTech Sample Notebook](https://cdn2.hubspot.net/hubfs/438089/notebooks/Samples/Miscellaneous/AdTech_Sample_Notebook_Part_1.html), as well as additional device attributes on which we can log alerts, for instance *device_battery* levels or *C02* levels. Unlike the dataset size in [Part 1](http://bit.ly/1RhErbF), I upload close to 200K devices, curtailing from original 2M entries, as a smaller dataset for rapid prototyping.
// MAGIC 
// MAGIC Again, all code is availabe on my github:
// MAGIC * [Python Scripts](https://github.com/dmatrix/examples/tree/master/py/ips)
// MAGIC * [Scala Libraries](https://github.com/dmatrix/examples/tree/master/scala/src/main/scala)
// MAGIC * [Scala Notebooks](https://github.com/dmatrix/examples/tree/master/spark/databricks/notebooks/scala)
// MAGIC 
// MAGIC Beside getting this notebook from [github](https://github.com/dmatrix/examples/blob/master/spark/databricks/notebooks/scala/IoTDeviceGeoIPDS.scala) or importing from here into your Databricks Cloud, you can also watch a [screencast](https://youtu.be/5cas87tpCt4)

// COMMAND ----------

// MAGIC %md ####Importing packages

// COMMAND ----------

import main.scala._
import main.scala.DeviceIoTData
import main.scala.DeviceAlerts
import org.apache.spark.{SparkContext, SparkConf}

// COMMAND ----------

// MAGIC %md ####Reading JSON as a Dataset

// COMMAND ----------

// MAGIC %md Use the Scala case class *DeviceIoTData* to convert the JSON device data into a Scala object. Of note here is GeoIP information for each device entry:
// MAGIC * IP address
// MAGIC * ISO-3166-1 two and three letter codes
// MAGIC * Country Name
// MAGIC * Latitude and longitude
// MAGIC 
// MAGIC With these attributes as part of the device data, we can map and visualize them as needed. For each IP associated with a *device_id*, I optained the above attributes from a webservice at http://freegeoip.net/csv/ip
// MAGIC 
// MAGIC *{"device_id": 198164, "device_name": "sensor-pad-198164owomcJZ", "ip": "80.55.20.25", "cca2": "PL", "cca3": "POL", "cn": "Poland", "latitude": 53.080000, "longitude": 18.620000, "scale": "Celsius", "temp": 21, "humidity": 65, "battery_level": 8, "c02_level": 1408, "lcd": "red", "timestamp" :1458081226051 }*

// COMMAND ----------

// create global accumulators Accumulator[Int]to keep track of alerts
val batteryCounts = sc.accumulator(0)
val c02Counts = sc.accumulator(0)

//fetch the JSON device information uploaded into the Filestore
val jsonFile = "/FileStore/tables/23lc6thg1458443979644/iot_devices.json"
//read the json file and create the dataset from the case class DeviceIoTData
// ds is now a collection of org.apache.spark.sql.Row
val ds = sqlContext.read.json(jsonFile).as[DeviceIoTData]
//display Dataset's 1000 rows
display(ds)

// COMMAND ----------

// MAGIC %md #### Iterating, transforming, and filtering Dataset

// COMMAND ----------

// MAGIC %md Let's iterate over the first 10 entries with the foreach() method and print them

// COMMAND ----------

ds.take(10).foreach(println(_))

// COMMAND ----------

// MAGIC %md Because the Dataset API in 1.6.1 is experimental, and not all relational query functionality for writing complex expressions available since it is still under flux and development, I have elected, where necessary, to convert Dataset to its equivalent DataFrame. Against this DataFrame, I write in-depth relation expressions.
// MAGIC 
// MAGIC For all relational expressions, the [Catalyst Optimizer](https://databricks.com/blog/2015/04/13/deep-dive-into-spark-sqls-catalyst-optimizer.html) will formulate an optimized logical and physical plan for execution, and [Tungsten](https://databricks.com/blog/2015/04/28/project-tungsten-bringing-spark-closer-to-bare-metal.html) engine will optimize the generated code. For our *DeviceIoTData*, it will use its standard encoders to optimize its binary internal representation, hence decrease the size of generated code, minimize the bytes transfered over the networks between nodes, and execute faster.
// MAGIC 
// MAGIC For instance, let's first filter the device dataset on *temp* and *humidity* attributes with a predicate, convert the resulting Dataset into its DataFrame, and then select column by names, order by temperature (in a descending order), and, finally display the first 10 items.

// COMMAND ----------

// MAGIC %md Filter over temperature and humidity, convert to DF, order by temp, and display 10 items

// COMMAND ----------

//issue select, map, filter, foreach operations on the datasets, just as you would for DataFrames
// convert the dataset to dataframe and use simple column name select() method.
val dsTemps = ds.filter(d => {d.temp > 30 && d.humidity > 70}).toDF().select("device_name", "device_id", "temp", "humidity").orderBy($"temp".desc)
// some the new transformed dataset
display(dsTemps)


// COMMAND ----------

// MAGIC %md By contrast, in the next cell, I don't convert to Dataframe but use Dataset APIs for filtering: take(10) returns an Array[DeviceIoTData]; using a foreach() method on the Array collection, I print each item.

// COMMAND ----------

//filter out dataset rows that meet the temperature and humimdity predicate
val dsFilter = ds.filter (d => {d.temp > 30 && d.humidity > 70}).take(10).foreach(println(_))

// COMMAND ----------

// MAGIC %md ### Defining a High-order Scala function

// COMMAND ----------

// MAGIC %md To illustrate the functional nature of Scala, let's define a function which we can use to log (and alert) battery replacements for
// MAGIC devices whose battery levels <= 1. This function can be passed as value parameter to *map(), filter(),* and, *foreach()* methods.

// COMMAND ----------

def logAlerts(log: java.io.PrintStream = Console.out, row: org.apache.spark.sql.Row, alert: String, notify: String ="kafka"): Unit = {
  val message = "[***ALERT***: %s : device_name: %s; device_id: %s ; cca3: %s]" format(alert, row(0), row(1), row(2))
  log.println(message)
  notify match {
      case "twilio" => DeviceAlerts.sendTwilio(message)
      case "snmp" => DeviceAlerts.sendSNMP(message)
      case "post" => DeviceAlerts.sendPOST(message)
      case "kafka" => DeviceAlerts.publishOnConcluent(message)
      case "pubnub" => DeviceAlerts.publishOnPubNub(message)
  }
  //update accumulators
  if (message.contains("BATTERY"))
    batteryCounts += 1
  if (message.contains("C02"))
    c02Counts += 1
}

// COMMAND ----------

// MAGIC %md Check the cluster's stderr log, where these alert messages
// MAGIC are logged.
// MAGIC 
// MAGIC *Note* that for this notebook I'm logging to stderr, but in production I could do an HTTP POST to a listening or monitoring service, issue SNMP alerts, send a SMS via [Twilio](http://www.twilio.com), notify Nagios service, publish it on [PuNub](https://www.pubnub.com/) or [Confluent](http://www.confluent.io/) publish/subscribe networks and platforms. What platform you choose is a function of your operational needs. 

// COMMAND ----------

//filter dataset rows with battery level == 0 and apply our defined funcion to each element to log an alert.
val dsBatteryZero = ds.filter(d => {d.battery_level <= 1}).toDF().select("device_name", "device_id", "cca3").foreach(d => logAlerts(Console.err, d, "REPLACE DEVICE BATTERY", "twilio"))
println("Total Bad Batteries = " + batteryCounts.value)

// COMMAND ----------

// MAGIC %md Again, check the cluser logs for C02 alerts

// COMMAND ----------

// filter datasets with dangerous levels of C02 and apply our defined function to each element to log an alert.
val dsHighC02Levels = ds.filter(d => {d.c02_level >= 1400 && d.lcd == "red"}).toDF().select("device_name", "device_id", "cca3").foreach(d => logAlerts(Console.err, d, "DEVICE DETECTS HIGH LEVELS OF C02 LEVELS", "pubnub"))
println("Total High Levels of C02 = " + c02Counts.value)

// COMMAND ----------

// MAGIC %md #### Issuing a complex relational query

// COMMAND ----------

// MAGIC %md Since I'm having loads of fun and feeling adventurous, I'll string together a relational query expression on my Dataset. For instance, do a filter, convert to DataFrame, groupBy, and compute average.
// MAGIC 
// MAGIC Look at Spark Jobs' DAGs to visualize the stages and where Tungsten comes into play to execute the physical plan.

// COMMAND ----------

// apply filter, convert to dataframe, groupBy, and compute average
val dsGroupBy = ds.filter ( d => {d.temp > 30 && d.humidity > 70} ).toDF().groupBy($"cca3").avg("temp")
display(dsGroupBy)

// COMMAND ----------

// MAGIC %md #### Visualizing datasets

// COMMAND ----------

// MAGIC %md **Finally, the fun bit!**
// MAGIC 
// MAGIC Data without visualization, withouth a narrative arc, to infer insights or to see a trend is useless. We always desire to make sense of the results.
// MAGIC 
// MAGIC By saving our Dataset, after converting to DataFrame, as a temporary table, I can issue complex SQL queries against it and visualize the results, using notebook's myriad plotting options.

// COMMAND ----------

ds.toDF().registerTempTable("iot_device_data")

// COMMAND ----------

// MAGIC %md Count all devices for a partiular country and map them

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data group by cca3 order by device_id desc limit 1000

// COMMAND ----------

// MAGIC %md Filter on humidity and C02 levels on only devices of that match the pattern *therm%* (in other words only therm-sticks), order them by humitdity in a descending order.

// COMMAND ----------

// MAGIC %sql select cca3, c02_level, device_name, humidity from iot_device_data where humidity > 80 and c02_level > 1400 and device_name like 'therm-stick%' order by humidity, c02_level desc limit 1000

// COMMAND ----------

// MAGIC %md Let's visualize the results as a pie chart and distribution for devices in the country where C02 are high.

// COMMAND ----------

// MAGIC %sql select cca3, c02_level from iot_device_data where c02_level > 1400 order by c02_level desc

// COMMAND ----------

// MAGIC %md Select all countries' devcices with high-levels of C02 and group by cca3 and order by device_ids 

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where lcd == 'red' group by cca3 order by device_id desc limit 100

// COMMAND ----------

// MAGIC %md find out all devices in countries whose batteries need replacements 

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where battery_level == 0 group by cca3 order by device_id desc limit 100

// COMMAND ----------

// MAGIC %md ####Conclusion
// MAGIC 
// MAGIC In this two part series of notebooks, we got a glimpse of simple ways to use Spark, saw the potential to process relatively large structured dataset garnered from IoT connected devices, got a feel for DataFrames and Dataset APIs, and with relative easy and comfort visualize all our results, all from within Databricks Cloud Community Edition, without us provisioning any clusters on prem.
// MAGIC 
// MAGIC Though the dataset generated was a simulation, it does not preclude you from doing a prototype or POC using real datasets, garnered from your data sources. For the outcome would be no different; only the manner in which you transform your data with the APIs and your Scala case class that reflects the inherent schema in your real JSON data would be different. 
// MAGIC 
// MAGIC If you haven't signed up for [Databricks Community Edition](http://go.databricks.com/databricks-community-edition-beta-waitlist), what you waiting for? 

// COMMAND ----------

// MAGIC %md ####What's next?
// MAGIC I want to use Google Maps library to map device's longitude and latitude as markers on a global map. Your ideas how to are welcome.
// MAGIC DM me at [@2twitme](https://twitter.com/2twitme).
// MAGIC 
// MAGIC Also, I want to use Spark 2.0 Structured Streaming, where these device events are injested as streams into a Notebook or a Spark streaming application. Instead of uploading a JSON file and then processing its state, the above SQL/DataFrame state queries can be done in real time. In their keynote at Spark Summit NY, 2016, both [Matei Zaharia](https://youtu.be/ZFBgY0PwUeY?t=795) and [Reynold Xin](https://youtu.be/oXkxXDG0gNk?t=418) refer to it as [continuous application](https://youtu.be/ZFBgY0PwUeY?t=795), which is not just streaming but an end-to-end application that handles streaming combined with Spark SQL/DataFrame ad-hoc queries and operations on a continuous DataFrame stream?all within a single application.
// MAGIC 
// MAGIC That's the future of Spark Structured Streaming, according to [Reynold Xin](https://youtu.be/oXkxXDG0gNk) and [Michael Armbrust](https://www.youtube.com/watch?v=i7l3JQRx7Qw&feature=youtu.be) 
