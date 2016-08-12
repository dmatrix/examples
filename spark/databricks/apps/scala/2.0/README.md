### Building a simple SparkSession Example for Apache Spark 2.0

In this directory is simple sample Spark application that demonstrates SparkSession introduced in Apache Spark 2.0. Since in Spark 2.0 the primary
level of abstraction are DataFrames and Datasets, built on top of Spark SQL engine, SparkSession provides a consolidated and encapsulated access to
manipulate data in your Spark Applications.

### Note: 
There's also a 1.6 version in this directory that will run with in a Spark 2.0 environment, suggesting it's backward compaitble.

### Using DataFrames.
To see how you can use DataFrame's and Dataset's high-level declarative API, this small sample code is a good introduction as well as a guide how to use 
SparkSession. Follow the steps below to build it and subsequently run it on your local machine with Spark in local mode. It presumes you have Spark 2.0 installed locally on your Mac.

* `sbt clean package`
* `spark-submit --class main.scala.zips.SparkSessionZipsExample --master local[6] target/scala-2.10/main-scala-zips_2.10-1.0.jar data/zips.json`
* `spark-submit --class main.scala.zips.SparkSessionZipsExample_16 --master local[6] target/scala-2.10/main-scala-zips_2.10-1.0.jar data/zips.json`


