### Building a simple SparkSession Example for Apache Spark 2.0

In this directory is simple sample Spark application that demonstrates SparkSession introduced in Apache Spark 2.0. Since in Spark 2.0 the primary
level of abstraction is DataFrames and Datasets, built on top of Spark SQL engine, SparkSession provides a consolidated and encapsulated access to
access and manipulate data in your Spark Applications.

### Using DataFrames.
To see how you can use relational operators and expression with DataFrame's API, this small sample code is a good introduction. Follow the steps below to build
it and subsequently run it on your local machine with Spark in local mode. It presumes you have Spark 2.0 installed locally on your Mac.

* `sbt clean package`
* `spark-submit --class main.scala.zips.SparkSessionSimpleZipsExample --master local[6] target/scala-2.10/main-scala-zips_2.10-1.0.jar data/zips.json`

