

"""
This short example demonstrates how to consume a json dataset stream directory from a pubnub publisher that write dataset files 
into its destination directory.

It's counter part, PubNub publisher, pub_dev_words.py publishes to a channel devices and also dumps it to a data directory
for this Spark Streaming program to consume. While it does not diretory use PubNub subscriber API to get the item, the next
step is to modify this app so that it employ's PubNub's subscribe channel to recieve publishe data.

(At the moment I having trouble getting publish.subscribe to work. Some problem with the keys. But for now this work around should
for running both publisher (pub_dev_workds.py) and subscribler (pubnub_dir_streamingpy) on the same machine)

Ideally, you want the this Spark app to run on the cluster and directly subscrbe from the PubNub Data Network Stream.
(stay tune... coming soon)


Though short and simple, it illustrates Spark's brevity in doing more with little. 

Simplicity does not preclude profundity. Once can achieve a lot by doing little, and that has been the appeal and draw of Spark Core API.

Author: Jules S. Damji

    `$ bin/spark-submit pubnub_dir_streaming.py data_dir`
"""
from __future__ import print_function

import os
import sys
import traceback

from pyspark import SparkContext
from pyspark.streaming import StreamingContext
from pyspark.sql import SQLContext

#
# TODO: integrate influx db insertion here as timeseries 
#
def insert_into_influxdb(item):
    print ("influxdb: inserted %s" % (item))

#
# get the sqlcontext if we want to use dataframes
#
def getSqlContextInstance(sparkContext):
    if ('sqlContextSingletonInstance' not in globals()):
        globals()['sqlContextSingletonInstance'] = SQLContext(sparkContext)
    return globals()['sqlContextSingletonInstance']

#
# main program
#
if __name__ == "__main__":
    if len(sys.argv) != 2:
        print("Usage: pubnub_dir_streaming data_dir ", file=sys.stderr)
        exit(-1)
    data_dir = sys.argv[1]
    sc = SparkContext(appName="pubnub_dir_streaming_app")
    ssc = StreamingContext(sc, 30)

    devicesRDD = ssc.textFileStream(data_dir)

    # Convert RDDs of the JSON DStream to DataFrame and run SQL query
    def process(time, rdd):
        try:
            for item in rdd.collect():
                insert_into_influxdb(item)
        except Exception as e:
            print(traceback.format_exc())
    #
    # peek the 10 elements
    #
    devicesRDD.foreachRDD(process)
    ssc.start()
    ssc.awaitTermination()
