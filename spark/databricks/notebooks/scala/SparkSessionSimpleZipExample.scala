// Databricks notebook source exported at Thu, 11 Aug 2016 01:23:25 UTC
// MAGIC %md #How to Use SparkSession - A Unified Entry Point in Apache Spark 2.0#
// MAGIC In Spark 2.0, we introduced SparkSession, a new entry point that subsumes SparkContext, SQLContext and HiveContext. For backward compatibiilty, the two are preserved. SparkSession has many features, and here we demonstrate some of the more important ones, using some data to illustrate its access to underlying Spark functionality. Even though, this notebook is written in Scala, similar functionality and APIs exist in Python and Java.
// MAGIC 
// MAGIC In Databricks notebooks and Spark REPL, the SparkSession is created for you, and accessible through a variable called *spark.*
// MAGIC 
// MAGIC To read the companion blog post, [click here](), and  its corrosponding Spark Application at [github](https://github.com/dmatrix/examples/tree/master/spark/databricks/apps/scala/2.0)

// COMMAND ----------

// MAGIC %md ##PART 1: Exploring SparkSession##

// COMMAND ----------

spark

// COMMAND ----------

// MAGIC %md ###SparkContext part of SparkSession###
// MAGIC Preserved as part of SparkSession for backward compatibility. 

// COMMAND ----------

spark.sparkContext

// COMMAND ----------

// MAGIC %md ###sqlContext as part of SparkSession### 
// MAGIC Preserved as part of SparkSession for backward compatibility

// COMMAND ----------

spark.sqlContext

// COMMAND ----------

// MAGIC %md ###SparkConf as part of SparkSession###
// MAGIC You can set/get configuration variables

// COMMAND ----------

spark.conf.set("spark.notebook.name", "SparkSessionSimpleZipExample")

// COMMAND ----------

spark.conf.get("spark.notebook.name")

// COMMAND ----------

spark.conf.get("spark.sql.warehouse.dir")

// COMMAND ----------

// MAGIC %md Spark config variables set can be accessed via SQL with variable subsitution

// COMMAND ----------

// MAGIC %sql select "${spark.notebook.name}, ${spark.sql.warehouse.dir}"

// COMMAND ----------

// MAGIC %md ###Creating DataFrames and Datasets###
// MAGIC There are a number of ways to create DataFrames and Datasets using the [SparkSession APIs](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.SparkSession). Once either [DataFrame](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.DataFrame) or [Dataset](http://spark.apache.org/docs/latest/api/scala/index.html#org.apache.spark.sql.Dataset) is created, you can manipulate your data.

// COMMAND ----------

import org.apache.spark.sql.functions._

// COMMAND ----------

val numDS = spark.range(5, 100, 5)
display(numDS.orderBy(desc("id")))

// COMMAND ----------

// MAGIC %md ### Create Summary Descriptive Stats###

// COMMAND ----------

display(numDS.describe())

// COMMAND ----------

val langPercentDF = spark.createDataFrame(List(("Scala", 35), ("Python", 30), ("R", 15), ("Java", 20)))

// COMMAND ----------

val lpDF = langPercentDF.withColumnRenamed("_1", "language").withColumnRenamed("_2", "percent")
display(lpDF.orderBy(desc("percent")))

// COMMAND ----------

// MAGIC %md ##PART 2: Exploring Zip codes data using SparkSession and Dataset APIs.##

// COMMAND ----------

// MAGIC %md Next, we going to exlore some zip code data fetched from [MongoDB](http://mongodb.com) via some code from [Newcircle](http://newcircle.com)

// COMMAND ----------

// MAGIC %sh wget http://media.mongodb.org/zips.json

// COMMAND ----------

// MAGIC %md The above command runs on your cluster's single node, fetches the zip code file from the specified URL, unzips in /databricks/driver directory

// COMMAND ----------

// MAGIC %sh ls /databricks/driver

// COMMAND ----------

import org.apache.spark.sql.catalyst.encoders.ExpressionEncoder
import org.apache.spark.sql.Encoder
import org.apache.spark.sql.types._
import org.apache.spark.sql.functions._
// A case class for zips data
case class Zips(zip:String, city:String, loc:Array[Double], pop:Long, state:String)

// COMMAND ----------

// MAGIC %md ###Reading the file with SparkSession###
// MAGIC Read the JSON file, infer the schema and convert it into a Dataset dictated by the case class Zips

// COMMAND ----------

val zipDF = spark.read.json("file:///databricks/driver/zips.json").withColumnRenamed("_id","zip")
//rename the _id to zip for readability
val zipDS = zipDF.withColumnRenamed("_id","zip").as[Zips]
// since we will quering this dataset often let's cache it
zipDS.cache()
display(zipDS)

// COMMAND ----------

// MAGIC %md **Q1**: Can you display states, zips, cities with population greater than 40000, in descending order

// COMMAND ----------


display(zipDS.select("state", "city", "zip", "pop").filter("pop > 40000").orderBy(desc("pop")))

// COMMAND ----------

// MAGIC %md 

// COMMAND ----------

// MAGIC %md **Q2**: Which cities and zips in the state of california are most populous? 

// COMMAND ----------

display(zipDS.select("city", "zip", "pop").filter('state === "CA").orderBy(desc("pop")))

// COMMAND ----------

// MAGIC %md **Q3**: Can you sum up the population of all the states and order them in descending order?

// COMMAND ----------

display(zipDS.select("state", "pop").groupBy("state").sum("pop").orderBy(desc("sum(pop)")))

// COMMAND ----------

// MAGIC %md ##PART 3: Creating Hive Table and querying it using SparkSession and Spark SQL APIs##

// COMMAND ----------

// MAGIC %md drop the table if one exists

// COMMAND ----------

spark.sql("DROP TABLE IF EXISTS hive_zips_table")

// COMMAND ----------

zipDS.write.saveAsTable("hive_zips_table")

// COMMAND ----------

display(spark.catalog.listDatabases)

// COMMAND ----------

// MAGIC %md **Q1**: Can you query the Hive table with the Spark SQL query indentical to the one above **Q1**?

// COMMAND ----------

display(spark.sql("SELECT state, city, zip, pop FROM hive_zips_table WHERE pop > 40000 ORDER BY pop DESC"))

// COMMAND ----------

// MAGIC %md **Q2:** Find the populus cities in Calfornia with total number of zips using the hive table?

// COMMAND ----------

display(spark.sql("SELECT COUNT(zip), SUM(pop), city FROM hive_zips_table WHERE state = 'CA' GROUP BY city ORDER BY SUM(pop) DESC"))

// COMMAND ----------

// MAGIC %sql SELECT COUNT(zip), SUM(pop), city FROM hive_zips_table WHERE state = 'CA'
// MAGIC   GROUP BY city 
// MAGIC   ORDER BY SUM(pop) DESC

// COMMAND ----------

// MAGIC %md **Q3**: Can you compose the same query as **Q2** using Datasets APIs?

// COMMAND ----------

display(zipsDS.<FILL_YOUR_CODE_HERE)

// COMMAND ----------

// MAGIC %md ###Thank you and have fun!###

// COMMAND ----------


