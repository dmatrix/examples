
#!/usr/bin/env python
#
#
import json
import sys
import time
from pyspark import SparkContext
from pyspark.streaming import StreamingContext

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
# Specify local mode and two cores
#
sc = SparkContext("local[2]", "pubnub_devices")

devicesRDD = sc.textFile(data_dir)
devices = devicesRDD.collect()
for d in devices:
	print_json(d)

