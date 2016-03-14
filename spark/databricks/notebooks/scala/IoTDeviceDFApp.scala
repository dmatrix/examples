// Databricks notebook source exported at Mon, 14 Mar 2016 21:21:45 UTC
// MAGIC %md #How to Process IoT Device Dataset Using Dataframes - Part 1

// COMMAND ----------

// MAGIC %md  This simple Databricks Scala Notebook shows the utility and ease with which you can read and process a large dataset of JSON file as structured data. Using [Dataframes](http://spark.apache.org/docs/latest/sql-programming-guide.html#dataframes) to represent a columnar structure, you can filter your dataset with predicates, select desired columns, and query data as though you were using a query langauge against an SQL table. Underneath, Spark creates an optimized logical and physical plan, using Tungsten, to execute an optimized query. 
// MAGIC 
// MAGIC Alternatively, you can register a Dataframe as a temporary table and then issue SQL queries against it?all very easy and intuitive to use. 
// MAGIC 
// MAGIC **Note:** I've already uploaded a Scala library (main.scala) that I developed on my IDE. By creating a shared library and attaching it across my mini-cluster, my distributed code (or functions to the transformers) can access any classes they need from this jar. 
// MAGIC 
// MAGIC Also, to access that device dataset, I imported a 10K JSON file into the FileStore /FileStore/tables/cqqoij9j1457633994821/IoTDevices10K.json
// MAGIC 
// MAGIC And finally, to learn how to upload your own Scala library jar or upload a dataset files, peruse these links.
// MAGIC 
// MAGIC * [Upload your Jar as a Library](https://community.cloud.databricks.com/?o=8599738367597028#externalnotebook/https%3A%2F%2Fdocs.cloud.databricks.com%2Fdocs%2Flatest%2Fdatabricks_guide%2Findex.html%2302%2520Product%2520Overview%2F04%2520Libraries.html)
// MAGIC * [Upload your data](https://community.cloud.databricks.com/?o=8599738367597028#externalnotebook/https%3A%2F%2Fdocs.cloud.databricks.com%2Fdocs%2Flatest%2Fdatabricks_guide%2Findex.html%2302%2520Product%2520Overview%2F07%2520Tables.html)
// MAGIC 
// MAGIC 
// MAGIC To get a flavor of what the JSON device data looks like here's a sample, with each device entry per line:
// MAGIC 
// MAGIC {"device_id": 1, "device_name": "meter-gauge-11rb5TH", "timestamp":1456186227, "temp": 14, "scale": "Celius", "latitude": 57, "longitude": 57, "zipcode": 95239, "humidity": 63}
// MAGIC 
// MAGIC {"device_id": 2, "device_name": "sensor-pad-2v5hE1Aoe", "timestamp":1456186227, "temp": 11, "scale": "Celius", "latitude": 93, "longitude": 93, "zipcode": 95622, "humidity": 63}
// MAGIC 
// MAGIC {"device_id": 3, "device_name": "device-mac-3OB7FPs", "timestamp":1456186227, "temp": 29, "scale": "Celius", "latitude": 55, "longitude": 55, "zipcode": 95907, "humidity": 35}
// MAGIC 
// MAGIC {"device_id": 4, "device_name": "sensor-pad-4tbzRAtimQ", "timestamp":1456186227, "temp": 27, "scale": "Celius", "latitude": 91, "longitude": 91, "zipcode": 96920, "humidity": 91}
// MAGIC ....
// MAGIC 
// MAGIC The imported library is available on my [github](https://github.com/dmatrix/examples/tree/master/scala)
// MAGIC 
// MAGIC In the part 2, I'll augment and modify the JSON data with additional information suchs as ISO-3991-1 two and three digit country codes as well as GeoIP locations with valid IPs, replacing zipcodes. With the newer dataset with GeoIP information, we can create insigtful dashboards and displays. All GeoIPs information is valid, except for attributes like temperature, humidity, battery_level, device_name and device_id, which are randomly generated.
// MAGIC 
// MAGIC (I wrote a couple of blogs, explaining simulation of connected IoT devices: One for [PubNub](https://www.linkedin.com/pulse/pubnub-integration-apache-spark-influxdb-simulation-iot-damji?trk=pulse_spock-articles) and other for [Kafka](https://www.linkedin.com/pulse/publish-subscribe-data-pipeline-confluent-20kafka-09-influxdb-damji?trk=pulse_spock-articles))
// MAGIC 
// MAGIC For part 2, the augmented JSON version will look as follows:
// MAGIC 
// MAGIC {"device_id": 8, "device_name": "sensor-pad-8wjosm", "ip": "210.173.177.1", "cca2": "JP", "cca3": "JPN", "cn": "Japan", "latitude": 35.690000, "longitude": 139.690000, "scale": "Celius", "temp": 11, "humidity": 84, "battery_level": 1, "timestamp" :1457904780656 }
// MAGIC 
// MAGIC All code that generates these huge datasets is availabe on my github:
// MAGIC * [Python Scripts](https://github.com/dmatrix/examples/tree/master/py/ips)
// MAGIC * [Scala Libraries](https://github.com/dmatrix/examples/tree/master/scala/src/main/scala)

// COMMAND ----------

import main.scala._
import org.apache.spark.{SparkContext, SparkConf}

//fetch the JSON device information uploaded into the Filestore
val jsonFile = "/FileStore/tables/cqqoij9j1457633994821/IoTDevices10K.json"

//read the json file and create the dataframe
val df = sqlContext.read.json(jsonFile)

//show or display the datafram's schema as inferred by Spark after reading the JSON structured data
df.printSchema()
//show the tables's first 20 rows
df.show()

// COMMAND ----------

//display the the total number of rows in the dataframe
// this will trigger a job
println("Total number of devices read: " + df.count())

// COMMAND ----------

// MAGIC %md As you know the df.count() is an action, which will trigger the DAG to be executed and its results sent back to the driver (this notebook).
// MAGIC For example, you can click on **Spark Jobs** to view a the complete DAG exececution
// MAGIC ![DAG execution count action]() "Count DAG execution")

// COMMAND ----------

// MAGIC %md Now that we have a dataframe in place, let's perform dataframe query like operations using the [Dataframe 1.6 API](http://spark.apache.org/docs/latest/sql-programming-guide.html)

// COMMAND ----------

//select only device names
df.select("device_name").show()


// COMMAND ----------

//filter all devices who humidity is greater than 75 and show them
df.filter(df("humidity") > 75).show()


// COMMAND ----------

//group together all same zipcodes and count them
df.groupBy("zipcode").count().show()


// COMMAND ----------

//filter all devices with humidity greater than 75 and count them
df.filter(df("humidity") > 85).groupBy("humidity").count().show()


// COMMAND ----------

// filter and groupBy zipcodes
 df.groupBy("zipcode").count().show()

// COMMAND ----------

//filter all devices with temperature greater than 35 and count them
 df.filter(df("temp") > 30).groupBy("temp").count().show()

// COMMAND ----------

// MAGIC %md Now let's register our DataFrame as a temporary table and issue SQL queries against it.

// COMMAND ----------

//Register the dataframe as a table and issue SQL queries against it
 df.registerTempTable("iot_devices_table")

// COMMAND ----------

// MAGIC %md Issue an SQL query against our newly created temporary table

// COMMAND ----------

 val results = sqlContext.sql("SELECT device_id, device_name FROM iot_devices_table")

// COMMAND ----------

// MAGIC %md Using foreach(), iterate over each element in the result set and print the first 50 items

// COMMAND ----------

 results.map(t => "Device Id: " + t(0) + " Device Name: " + t(1)).take(50).foreach(println)


// COMMAND ----------

//issue select statements and then print the first 50 items from the results set
//results returned as four columns of strings[device_id, device_name, humidity, temp]
val results2 = sqlContext.sql("SELECT device_id, device_name, humidity, temp FROM iot_devices_table WHERE humidity  >= 85 AND temp <= 20")

// COMMAND ----------

// MAGIC %md As above iteratve our the result set using map() and print the first 50 entries

// COMMAND ----------

results2.map(t => "[Device Id: " + t(0) + " Device Name: " + t(1) + " Humidity: " +  t(2) + " Temp: " + t (3) + " ]").take(50).foreach(println)

// COMMAND ----------

println("Total number of devices selected: " + results.count())

// COMMAND ----------

println("Total number of devices selected: " + results2.count())

// COMMAND ----------

// MAGIC %md Lastly, I want to visualize the results from my queries. Whereas the plot options offers mulitple ways to visualize your dataset, here I have selected graphs stacked

// COMMAND ----------

// MAGIC %sql SELECT device_id, device_name, humidity, temp FROM iot_devices_table WHERE humidity  >= 85 AND temp <= 20

// COMMAND ----------

// MAGIC %sql SELECT device_id, zipcode FROM iot_devices_table WHERE humidity >= 65

// COMMAND ----------

// MAGIC %sql select device_id, device_name, humidity, temp, zipcode, timestamp from iot_devices_table where zipcode > 91000

// COMMAND ----------


