
package main.scala.zips

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.types._


/**
  * Created by jules on 8/9/2016
  * 
  A simple Spark 2.0 example shows use of SparkSession.
  *
  * All very easy and intuitive to use.
  * 
  * spark-submit --class main.scala.zips.SparkSessionSimpleZipsExample --master local[6] target/scala-2.10/main-scala-zips_2.10-1.0.jar <path_to_json_file>
  */
object SparkSessionSimpleZipsExample {

  def main(args: Array[String]): Unit = {

    if (args.length != 1) {
      println("Usage: SparkSessionSimpleExample <path_to_json_file")
      System.exit(1)
    }
    //get the JSON file
    val jsonFile = args(0)
    // set up the spark using SparkSession. No need to create SparkContext
    // You automatically get as part of the SparkSession
    // SparkSession uses the builder design to construct a SparkSession

     val spark = SparkSession
      .builder()
      .appName("SparkSessionZipsExample")
      .config("spark.some.config.option", "some-value")
      .getOrCreate()

    import spark.implicits._
    //read the json file, infere the schema and create a DataFrame
    val zipsDF = spark.read.json(jsonFile)

    //show the tables's first 20 rows
    zipsDF.show(10)

    //display the the total number of rows in the dataframe
    println("Total number of zipcodes: " + zipsDF.count())

    //filter all cities whose population > 40K
    zipsDF.filter(zipsDF.col("pop")> 40000).show(10)

    //Now create an SQL table and issue SQL queries against it without
    // explicting using SQLContext but from SparkSession
    // Creates a temporary view using the DataFrame
    zipsDF.createOrReplaceTempView("zips_table")

    println("SELECT city, pop, state, zip FROM zips_table WHERE pop > 40000")
    val resultsDF = spark.sql("SELECT city, pop, state, zip FROM zips_table WHERE pop > 40000")
     resultsDF.show(10)
  }
}


