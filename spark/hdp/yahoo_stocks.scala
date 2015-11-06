

import org.apache.spark.sql.hive.orc._  
import org.apache.spark.sql._ 

//create context and orc tables
//
val hiveContext = new org.apache.spark.sql.hive.HiveContext(sc)

hiveContext.sql("create table yahoo_orc_table (date STRING, open_price FLOAT, high_price FLOAT, low_price FLOAT, close_price FLOAT, volume INT, adj_price FLOAT) stored as orc")

//load the file and create a RDD

val yahoo_stocks = sc.textFile("hdfs://user/root/yahoo_stocks.csv")

//
//create a schema
//
val schemaString = "date open_price high_price low_price close_price volume adj_price"
val schema = StructType(schemaString.split(" ").map(fieldName => { 
  if (fieldName == "date") 
    StructField(fieldName, StringType, true) 
  else if(fieldName == "open_price") 
    StructField(fieldName, FloatType, true)
  else if (fieldName == "high_price")
    StructField(fieldName, FloatType, true)
  else if (fieldName == "low_price")
    StructField(fieldName, FloatType, true)
  else if (fieldName == "close_price")
    StructField(fieldName, FloatType, true)
  else if (fieldName == "volume")
    StructField(fieldName, IntegerType, true)
  else
     StructField(fieldName, FloatType, true)
  } ))
//
// generate stock rdds
//
val yahooStockRDD = yahoo_stocks.map(_.split(",")).map(p => Row(p(0), p(1), p(2), p(3), p(4), p(5), p(6) ) ) 

//
//schema for the yahoo stock table
//
val yahooStockSchemaRDD = hiveContext.applySchema(yahooStockRDD, schema)

//
//register as a temporary table
//
yahooStockSchemaRDD.registerTempTable("yahoo_stocks_temp")

//query against the table

val results = hiveContext.sql("SELECT * FROM yahoo_stocks_temp")

results.map(t => "Stock Entry: " + t.toString).collect().foreach(println) 
//
// save as ORC file
//
yahooStockSchemaRDD.saveAsOrcFile("yahoo_stocks_orc")