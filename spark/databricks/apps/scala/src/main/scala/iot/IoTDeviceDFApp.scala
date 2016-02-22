package main.scala.iot

import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by jules on 2/9/16.
  * This simple Spark app shows the utility and ease with which you can read and process a large dataset of JSON file as structured data.
  * Using dataframes to represent a columnar structure, you can filter with predicates, select, and process data as though you were
  * using a query langauge against an SQl table. Undeneath, Spark creates an optimized logical and physical plan, using Tungsten.
  *
  * Additionally, you can register the dataframe as a temporary table and then issue SQL like queries to it.
  *
  * All very easy and intuitive to use.
  * 
  * spark-submit --class main.scala.iot.IotDeviceDFApp --master local[6] target/scala-2.10/main-scala-iot_2.10-1.0.jar <path_to_json_file>
  */
object IoTDeviceDFApp {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: IotDeviceDFApp <path_to_json_file")
      System.exit(1)
    }
    //get the JSON file
    val jsonFile = args(0)
    //set up the spark contexts
    val sparkConf = new SparkConf().setAppName("IotDeviceRDDApp").setMaster("local")
    val sc = new SparkContext(sparkConf)
    val sqlContext = new org.apache.spark.sql.SQLContext(sc)

    //read the json file and create the dataframe
    val df = sqlContext.read.json(jsonFile)

    //show or display the datafram's schema as inferred by Spark after reading the JSON structured data
    df.printSchema()
    //show the tables's first 20 rows
    df.show()
    //display the the total number of rows in the dataframe
    println("Total number of devices read: " + df.count())

    //perform some operations on dataframes
    //select only device names
    df.select("device_name").show()

    //filter all devices who humidity is greater than 75 and show them
    df.filter(df("humidity") > 75).show()

    //group together all same zipcodes and count them
    df.groupBy("zipcode").count().show()

    //filter all devices with humidity greater than 75 and count them
    df.filter(df("humidity") > 85).groupBy("humidity").count()show()

    //filter all devices with temperature  greater than 35 and count them
    df.filter(df("temp") > 30).groupBy("temp").count()show()

    // filter by zipcodes
    df.groupBy("zipcode").count().show()

    //Now register the dataframe as a table and issue SQL queries against it
    df.registerTempTable("iot_devices_table")

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
