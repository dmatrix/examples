// Databricks notebook source exported at Wed, 16 Mar 2016 01:22:50 UTC
import main.scala._
import org.apache.spark.{SparkContext, SparkConf}


// COMMAND ----------

// MAGIC %md Create a case class so that we can use a Dataset and Dataframe interchangably

// COMMAND ----------

// case class for mapping to a Dataset for the JSON {"device_id": 198164, "device_name": "sensor-pad-198164owomcJZ", "ip": "80.55.20.25", "cca2": "PL", "cca3": "POL", "cn": "Poland", "latitude": 53.080000, "longitude": 18.620000, "scale": "Celius", "temp": 21, "humidity": 65, "battery_level": 8, "c02_level": 1408, "lcd": "red", "timestamp" :1458081226051 }
//
case class DeviceIotData (device_id: Long, device_name: String, ip: String, cca2: String, cca3: String, cn: String, latitude: Double, longitude: Double, scale:String, temp: Long, humidity: Long, battery_level: Long, c02_level: Long, lcd: String, timestamp: Long)

// COMMAND ----------

import main.scala._
import org.apache.spark.{SparkContext, SparkConf}

//fetch the JSON device information uploaded into the Filestore
val jsonFile = "/FileStore/tables/hll2y8jf1458081917577/iot_devices.json"

//read the json file and create the dataset
val ds = sqlContext.read.json(jsonFile).as[DeviceIotData]

//show Dataset's first 20 rows
ds.show(5)

// COMMAND ----------

//issue select, map, filter, foreach operations on the datasets, just as you would for Dataframes
// convert the dataset to dataframe and use simple column name selects with the select() method.
ds.toDF().select("device_name", "device_id", "temp", "humidity").orderBy($"temp").show(10)

// COMMAND ----------

//filter out dataset rows that meet the temperature and humimdity predicate
ds.filter (d => {d.temp > 30 && d.humidity > 70}).show(10)

// COMMAND ----------

//filter dataset rows with battery level == 0
ds.filter (d => {d.battery_level == 0}).toDF().select("device_name", "device_id", "cca3").show(10)

// COMMAND ----------

// a more sopisticated dataset query
ds.filter (d => {d.temp > 30 && d.humidity > 70}).toDF().groupBy($"cca3").avg("temp").show(10)

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data group by cca3 order by device_id desc limit 1000

// COMMAND ----------

// MAGIC %sql select cca3, c02_level, device_name, humidity from iot_device_data where humidity > 80 and c02_level > 1400 and device_name like 'therm%' order by humidity, c02_level desc limit 1000

// COMMAND ----------

// MAGIC %sql select cca3, c02_level from iot_device_data where c02_level > 1400 order by c02_level desc

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data group by cca3

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where lcd == 'red' group by cca3 order by device_id desc limit 100

// COMMAND ----------

// MAGIC %sql select cca3, count(distinct device_id) as device_id from iot_device_data where battery_level == 0 group by cca3 order by device_id desc limit 100

// COMMAND ----------


