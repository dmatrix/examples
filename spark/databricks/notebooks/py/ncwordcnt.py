
#!/usr/bin/env python
# import the right stuff
#
from pyspark import SparkContext
from pyspark.streaming import StreamingContext

# Specify local mode and two cores
#
sc = SparkContext("local[2]", "networkword")
# specifiy the interval of I second batches
ssc = StreamingContext(sc, 1)
#
# Create DStream of RDDS that will connect to the port 9999
#
lines = ssc.socketTextStream("localhost", 9999)
# Split each line into words
words = lines.flatMap(lambda line: line.split(" "))
#pair the words
pairs = words.map(lambda w: (w, 1))
#count each word by using reduceByKey
wordCounts = pairs.reduceByKey(lambda x, y: x + y)
#
# print some elments in the RDDS
#
wordCounts.pprint()

#start the reciever thread
ssc.start()
#waith for the socket to close
ssc.awaitTermination()
