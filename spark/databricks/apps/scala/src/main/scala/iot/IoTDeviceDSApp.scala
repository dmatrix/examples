package main.scala.iot

import org.apache.spark.{SparkContext, SparkConf}

//case class for the Device data
//"device_id": 12000, "device_name": "sensor-pad-12000gtYLp00o", "timestamp":1454965623, "temp": 20, "scale": "Celius", "latitude": 51, "longitude": 51, "zipcode": 94844, "humidity": 64}
case class DeviceData (device_id: Long, device_name: String, timestamp: Long, temp: Long, scale:String, latitude: Long, longitude: Long, zipcode: Long, humidity: Long)

/**
  * Created by jules on 2/9/16.
  * This simple Spark app shows the utility and ease with which you can read and process a large dataset of JSON file as structured data.
  * Using dataframes to represent a columnar structure, you can filter with predicates, select, and process data as though you were
  * using a query langauge. Additionally, you can register the dataframe as a temporary table and then issue SQL like queries to it.
  *
  * All very easy and intuitive to use.
  *
  * spark-submit --class main.scala.iot.IoTDeviceDSApp --master local[6] target/scala-2.10/spark-device-iot-app_2.10-1.0.jar <path_to_json_file>
  */
object IoTDeviceDSApp {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: IotDeviceDSApp <path_to_json_file")
      System.exit(1)
    }
    //get the JSON file
    val jsonFile = args(0)
    //set up the spark contexts
    val sparkConf = new SparkConf().setAppName("IotDeviceDSApp").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    //read the json file and create the dataframe
    import sqlContext.implicits._

    //convert the dataframe into a dataset using the case class DeviceData defined above.
    val ds = sqlContext.read.json(jsonFile).as[DeviceData]
    //show the datasets in a tabular form
    ds.show(20)

    //issue select, map, filter, foreach operations on the datasets, just as you would for RDDs
    // convert the dataset to dataframe and use simple column name selects with the select() method.
    ds.toDF().select("device_name", "device_id", "temp", "humidity").show(20)
    // filter out dataset rows that meet the temperature and humimdity predicate
    ds.filter (d => {d.temp > 30 && d.humidity > 70}).show(20)
    // a more complicated query
    ds.filter (d => {d.temp > 30 && d.humidity > 70}).toDF().groupBy($"zipcode").avg("temp").show(50)
    //use map() methods
    ds.map(d => {d.toString}).show(20)
    ds.map(d => {d.device_name}).show(20)
    ds.foreach(println(_))

    // register as a table
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
  }
}
