#PubNub integration with Apache Spark and InfluxDB

![](images/pubnub_spark.png "An Overview of Data Flow")

* Legend
- P = Publish
- S = Subscribe
- R = Read
- W = Write

##Introduciton
##Relevant Files
### pub_dev_words.py

This short example illustrates the simplicity of using PubNub Realtime Streaming Netowrk,
and how to use PubNub SDK to program the netowrk, to publish data streams or to subscribe data streams.

Though the example is simple, it simulates as though multiple devices are registering themselves or announcing their
availability by publishing their state on a dedicated channel. In reality this could be a deployment of meters or sensors
in an area code that you wish to monitor for tial and do some realtime analysis using Spark.

Also, as an optional extension, the app can write to a socket or a directory where a Spark Streaming context monitoring for live
live data streams of JSON objects from each device. For directory, the Spark application must run on the same JVM as this app and in local mode.

I employ a thread that simulates mulitple devices acting as publishers, but in reality each JSON data object could be published
separately by each device using PubNub's publish-subscribe API. 

It downloads a list of words from the Internet (http://www.textfixer.com/resources/common-english-words.txt) and uses them as device names. Each JSON object has the 
following format:

     {"device_id": 97, 
     "timestamp", 1447886791.607918,
     "lat": 22, 
     "long": 82, 
     "scale: 
     "Celius", 
     "temp": 22, 
     "device_name": "sensor-mac-word",
     "humidity": 15,
     "zipcode:" 95498
    }

 To run this program to create json files into the destinattion directory for Spark Streaming consumption:
 	`$ python pub_dev_words.py -u http://www.textfixer.com/resources/common-english-words.txt -c devices -i 1 -d data`

### pubnub_dir_streaming.py
 This short example demonstrates how to consume a json dataset stream directory from a pubnub publisher that write dataset files into its destination directory.

It's counter part, PubNub publisher, pub_dev_words.py publishes to a channel devices and also dumps it to a data directory
for this Spark Streaming program to consume. While it does not diretory use PubNub subscriber API to get the item, the next
step is to modify this app so that it employ's PubNub's subscribe channel to recieve publishe data.

(At the moment I having trouble getting publish.subscribe to work. Some problem with the keys. But for now this work around should
for running both publisher (pub_dev_workds.py) and subscribler (pubnub_dir_streamingpy) on the same machine)

Ideally, you want the this Spark app to run on the cluster and directly subscrbe from the PubNub Data Network Stream.
(stay tune... coming soon)


Though short and simple, it illustrates Spark's brevity in doing more with little. 

Simplicity does not preclude profundity. Once can achieve a lot by doing little, and that has been the appeal and draw of Spark Core API.

    `$ bin/spark-submit pubnub_dir_streaming.py data_dir`

##TO DO

1. Integrate with InfluxDB
2. Include Pubnub.subscribe() calls within the Spark Streaming App
