package main.scala.iot

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by jules on 3/24/16.
  *
  * This simple Spark app shows the utility and ease with which you can read and process a large dataset of JSON file as structured data.
  * Using DataFrames and Datasets to represent a columnar structure, you can filter with predicates, select, and process data as though you were
  * using a query language against an SQL table. Underneath, Spark creates an optimized logical and physical plan, using the Catalyst Optimizer, along
  * for the plan and with Tungsten for optimized code generation.
  *
  * The JSON data as additional device attributes—real IP addresses, GeoIP locations, and ISO-3661-1 counry codes—that are absent in
  * two related apps' device JSON file in this directory: IoTDeviceDSApp.scala and IoTDeviceDFApp.scala
  *
  * For example, the device.json looks likes:
  *
  * {"device_id": 1, "device_name": "meter-gauge-1xbYRYcj", "ip": "68.161.225.1", "cca2": "US", "cca3": "USA", "cn": "United States", "latitude": 38.000000, "longitude": -97.000000, "scale": "Celsius", "temp": 34, "humidity": 51, "battery_level": 8, "c02_level": 868, "lcd": "green", "timestamp" :1458444054093 }
  * {"device_id": 2, "device_name": "sensor-pad-2n2Pea", "ip": "213.161.254.1", "cca2": "NO", "cca3": "NOR", "cn": "Norway", "latitude": 62.470000, "longitude": 6.150000, "scale": "Celsius", "temp": 11, "humidity": 70, "battery_level": 7, "c02_level": 1473, "lcd": "red", "timestamp" :1458444054119 }
  *
  *
  * Additionally, you can register a DataFrame as a temporary table and then issue Spark SQL queries against it.
  *
  * All very easy and intuitive to use.
  *
  * spark-submit --class main.scala.iot.IoTDeviceGeoIPApp --master local[6] target/scala-2.10/main-scala-iot_2.10-1.0.jar <path_to_json_file>
  *
  */

object IoTDeviceGeoIPApp {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: IoTDeviceGeoIPApp <path_to_json_file")
      System.exit(1)
    }
    // get the JSON file
    val jsonFile = args(0)
    //set up the spark contexts
    val sparkConf = new SparkConf().setAppName("IoTDeviceGeoIPApp").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    //r ead the json file and create the dataframe
    import sqlContext.implicits._

    // convert the dataframe into a dataset using the case class DeviceData defined above.
    val ds = sqlContext.read.json(jsonFile).as[DeviceIoTData]
    //show the datasets in a tabular form
    ds.show(20)
    // issue select, map, filter, foreach operations on the datasets, just as you would for RDDs
    // convert the dataset to DataFrame and use simple column name selects with the select() method.
    ds.toDF().select("device_name", "device_id", "temp", "humidity").show(20)
    // filter out dataset rows that meet the temperature and humidity predicate
    ds.filter (d => {d.temp > 30 && d.humidity > 70}).show(20)
    // a more complicated query
    ds.filter (d => {d.temp > 30 && d.humidity > 70}).toDF().groupBy($"cca3").avg("temp").show(50)
    //filter by high levels of C02, using map() transform and select name, cca3, c02, and show 20 items
    ds.filter(d => { d.c02_level > 1400}).toDF().select("device_name", "cca3", "cn", "c02_level").show(20)

    // register as a table and issue Spark SQL queries against it.
    ds.toDF().registerTempTable("iot_devices_table")
    //issue select statements and then print the first 50 items from the results set
    //think of results returned as rows of columns. In this case, two columns [string, string] from all rows
    //that comprise the dataframe
    println("SELECT device_id, device_name FROM iot_devices_table")
    val results = sqlContext.sql("SELECT device_id, device_name FROM iot_devices_table")
    results.map(t => "Device Id: " + t(0) + " Device Name: " + t(1)).take(50).foreach(println)
    println("Total number of devices selected: " + results.count())

    //issue select statements and then print the first 50 items from the results set
    //results returned as four columns of strings[device_id, device_name, humidity, temp]
    println("SELECT device_id, device_name, humidity, temp FROM iot_devices_table WHERE humidity  >= 85 AND temp <= 20")
    val results2 = sqlContext.sql("SELECT device_id, device_name, humidity, temp FROM iot_devices_table WHERE humidity  >= 85 AND temp <= 20")
    results2.map(t => "[Device Id: " + t(0) + " Device Name: " + t(1) + " Humidity: " +  t(2) + " Temp: " + t (3) + " ]").take(50).foreach(println)
    println("Total number of devices selected: " + results2.count())

    //count distinct devices in countries where battery levels is zero and order them by device ids in a descending order
    print ("select cca3, count(distinct device_id) as device_id from iot_devices_table where battery_level == 0 group by cca3 order by device_id desc limit 100)")

    val results3 = sqlContext.sql("SELECT cca3, count(distinct device_id) as device_id FROM iot_devices_table WHERE battery_level = 0 group by cca3 order by device_id desc limit 100")
    results3.map(r => "[cca3: " + r(0) + " number of devices with bad batteries:" + r(1) + " ]").take(50).foreach(println(_))

    println ("SELECT cca3, count(distinct device_id) as device_id FROM iot_devices_table WHERE lcd = 'red' group by cca3 order by device_id desc limit 100")

    val results4 = sqlContext.sql("SELECT cca3, count(distinct device_id) as device_id FROM iot_devices_table WHERE lcd = 'red' group by cca3 order by device_id desc limit 100")
    results4.map(r => "[cca3: " + r(0) + " number of devices with dangerous levels of C02:" + r(1) + " ]").take(50).foreach(println(_))
  }
}
