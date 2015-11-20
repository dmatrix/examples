
#!/usr/bin/env python
#
#
"""
This short example demonstrates how to consume a json dataset, create a jsonRDD, a dataframe, and then use SQL like queries against 
a temporary table.

As a subscriber of the devices' info published by a PubNub collection of devices, this application can be used to some quick queries
Ideally, you will want to access this dataset stored in timeseries store like InfluxDB and then loaded into Databricks cloud to analyize
visualize the dataset

For now, this does the task. I have a Databricks notebook and respective HTML that shows some visualizaion:

1. https://github.com/dmatrix/examples/blob/master/spark/databricks/notebooks/py/pubnub_devices.py
2. https://github.com/dmatrix/examples/blob/master/spark/databricks/notebooks/py/sql_device_provisioning.html

Though short and simple, it illustrates Spark's brevity in doing more with little. 

Simplicity does not preclude profundity. Once can achieve a lot by doing little, and that has been the appeal and draw of Spark Core API.

Author: Jules S. Damji

"""
import json
import sys
import time
from pyspark import SparkContext
from pyspark.sql import SQLContext


def print_json(j):
	print json.dumps(j)

def process(rdd):
	print("========= %s =========" % time.ctime())
	rdd.map(lambda j: json.dumps(j))

if __name__ == "__main__":
  if len(sys.argv) != 2:
    print("Usage: pub_dev_words.py directory")
    sys.exit(-1)
#
# get the data diretory
#
data_dir = sys.argv[1]
#
# Specify local mode and two cores
#
sc = SparkContext("local[2]", "pubnub_devices")

devicesRDD = sc.textFile(data_dir)
#
# create a dataframe
#
sqlContext = SQLContext(sc) 
df = sqlContext.jsonRDD(devicesRDD)
#
# show the infered schema
#
df.show()
#
# register a temp table and issue some SQL like queries to our device information
#
df.registerTempTable("deviceTables")
#
# QUERY 1: get all devices with temp > 20 and humidity < 50
#
results = sqlContext.sql("select device_id, device_name, humidity, temp from deviceTables where temp > 20 and humidity < 50")
# results of a SQL query is another RDD
# interate over the results
#
for r in results.collect():
	print r
#
# QUERY 2: get devices with that exceed a certain zone
#
results = sqlContext.sql("select device_id, device_name, humidity, temp, zipcode, timestamp from deviceTables where zipcode > 92000")
for r in results.collect():
	print r





