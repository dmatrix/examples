
#!/usr/bin/env python
# import the right stuff
#
import json
from pyspark import SparkContext
from pyspark.streaming import StreamingContext

def print_json(j):
	json.dumps(j)

# Specify local mode and two cores
#
sc = SparkContext("local[2]", "pubnub_devices")
# specifiy the interval of I second batches
ssc = StreamingContext(sc, 1)
#
# Create DStream of RDDS that will connect to the port 9999
#
devicesRDD = ssc.socketTextStream("localhost", 9999)
devicesJSONRDD = devicesRDD.map(lambda j: print_json(j))

#start the reciever thread
ssc.start()
#waith for the socket to close
ssc.awaitTermination()
