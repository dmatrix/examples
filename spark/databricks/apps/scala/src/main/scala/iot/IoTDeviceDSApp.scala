package main.scala.iot

import org.apache.spark.{SparkContext, SparkConf}

//case class for the Device data
//"device_id": 12000, "device_name": "sensor-pad-12000gtYLp00o", "timestamp":1454965623, "temp": 20, "scale": "Celius", "lat": 51, "lng": 51, "zipcode": 94844, "humidity": 64}
case class DeviceData (device_id: Long, device_name: String, timestamp: Long, temp: Long, scale:String, lat: Long, lng: Long, zipcode: Long, humidity: Long)

/**
  * Created by jules on 2/9/16.
  * This simple Spark app shows the utility and ease with which you can read and process a large dataset of JSON file as structured data.
  * Using dataframes to represent a columnar structure, you can filter with predicates, select, and process data as though you were
  * using a query langauge. Additionally, you can register the dataframe as a temporary table and then issue SQL like queries to it.
  *
  * All very easy and intuitive to use.
  *
  * spark-submit --class main.scala.iot.IoTDeviceDSApp --master local[6] target/scala-2.10/main-scala-iot_2.10-1.0.jar <path_to_json_file>
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
    ds.printSchema()

    ds.filter(_.humidity > 70).show()
  }
}
