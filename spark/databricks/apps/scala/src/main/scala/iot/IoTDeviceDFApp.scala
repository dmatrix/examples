package main.scala.iot

import org.apache.spark.sql.SQLContext
import org.apache.spark.{SparkContext, SparkConf}

/**
  * Created by jules on 2/9/16.
  * spark-submit --class main.scala.iot.IoTDeviceDFApp --master local[6] target/scala-2.10/main-scala-iot_2.10-1.0.jar <path_to_json_file>
  */
object IoTDeviceDFApp {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: IotDeviceRDDApp <path_to_json_file")
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
  }
}
