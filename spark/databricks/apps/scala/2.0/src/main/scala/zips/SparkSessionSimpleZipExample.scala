
package main.scala.zips

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._


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

     val warehouseLocation = "file:${system:user.dir}/spark-warehouse"

     val spark = SparkSession
      .builder()
      .appName("SparkSessionZipsExample")
      .config("spark.sql.warehouse.dir", warehouseLocation)
      .enableHiveSupport()
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

    // let's cache
    zipsDF.cache()

    //find the all populous cites and zip in the state of california and order them  by population

    zipsDF.select("city", "zip", "pop").filter('state === "CA").orderBy(desc("pop")).show(10)

    //Now create an SQL table and issue SQL queries against it without
    // explicting using SQLContext but from SparkSession
    // Creates a temporary view using the DataFrame
    zipsDF.createOrReplaceTempView("zips_table")

    println("SELECT city, pop, state, zip FROM zips_table WHERE pop > 40000")
    val resultsDF = spark.sql("SELECT city, pop, state, zip FROM zips_table WHERE pop > 40000")
    resultsDF.show(10)

    //drop the table if exists to get around existing tabel error
    spark.sql("DROP TABLE IF EXISTS zips_hive_table")
     //Now save as a hive table
     spark.table("zips_table").write.saveAsTable("zips_hive_table")
     // make a query to the hive table now
     val resultsHiveDF = spark.sql("SELECT city, pop, state, zip FROM zips_hive_table WHERE pop > 40000")
     resultsHiveDF.show(10)

     //Find the populus cities in Calfornia with total number of zips using the hive table
     spark.sql("SELECT COUNT(zip), SUM(pop), city FROM zips_hive_table WHERE state = 'CA' GROUP BY city ORDER BY SUM(pop) DESC").show(10)

     // register a simple UDF
     spark.udf.register("cityLength", (c:String) => c.length())
     val resultsCityLens = spark.sql("SELECT city, cityLength(city) as city_length FROM zips_hive_table ORDER BY city_length DESC")
     resultsCityLens.show(10)

  }
}


