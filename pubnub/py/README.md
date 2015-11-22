#PubNub Integration with Apache Spark and InfluxDB

![](images/pubnub_spark.png "An Overview of Data Flow")

Legend
- P = Publish
- S = Subscribe
- R = Read
- W = Write

***my woeful attempt to use [53 Pencil & Paper Sketch!] (https://www.fiftythree.com/)***

##Introduction
For as long as there have been papers, there have been publishers and consumers. 
Back then the King's scribes were the publishers, the pigeon the courier or transport, and Lords of the Houses the consumers. In modern times, in the digital era, data is securely and reliably published and selectively subscribed. In other words, the publish/subscribe paradigm is not new; it's ancient.

Today's products such Tibco, Java Messaging Service (JMS), RabbitMQ, Apache Kafka, Amazon SQS. etc are examples of frameworks and platforms built on this paradigm for publishing and subscribing data and writing streaming applications.

Add to that list a realtime streaming data network—and you get global, scalable, and reliable messaging network with low-latency, allowing you to build and connect devices for realtime applications quickly and easily. One such data streaming and messaging network is [PubNub.] (http://pubnub.com)

I tried using it to publish (or simulate) realtime sensor or device data, using its [Python SDK](https://www.pubnub.com/developers/), to write my first Publish-Subscribe app. To make things interesting, I went futher to integrate the app with [Apache Spark Streaming] (http://apache.spark.org)—and soon with [InfluxDB](http://influxdb.com). 

The diagram above shows the dataflow (Ah, my first woeful attempt to use [53 Pencil & Paper Sketch!] (https://www.fiftythree.com/)

Ideally, I'm going to want to subcribe to a published channel from within the Spark Streaming App. For now, because of debugging with PubNub pub/sub keys' issues, I'm cheating slightly, by publishing dataset to a local file system directory where the local Spark appplication monitors the directory for any published datasets.

Note that in this scenario, you can't run Spark Streaming App in a clustered or standlone mode. The Spark app must run in local mode on the same machine as pubnub publisher app.
##Device Simulation
I simulate as though a large installation of sensors and devices in a particular zipcode area is publishing the state, temperature, and humidity data onto the PubNub Data Network on a well-known public channel "devices."

Interested parties can subscribe to this channel, particularly any app that's monitoring or provisioning devcies. In this case,I have my Spark Streaming App monitoring a directory.

Additionally, this Spark App (or any subsriber) can insert data into a timeseries database such as InfluxDB (on the to do list) for data visualization over period of time, which is useful for trending and monitoring usage.

For example, using the datasets published by this app with [Databricks Notebook Python] (https://github.com/dmatrix/examples/blob/master/spark/databricks/notebooks/py/sql_device_provisioning.ipynb) and [DataFrames & SQL] (http://spark.apache.org/docs/latest/sql-programming-guide.html), I can visualize different data fields. Below are few examples:


![](images/screen_3.png "Temperature vs Devices")

![](images/screen_1.png "Temperature, Humidity vs Devices")

![](images/screen_2.png "Humidity vs Zipcode")


##Relevant Files
###pub_dev_words.py (Publisher)

This short example illustrates the simplicity of using PubNub Realtime Streaming Netowrk,
and how to use PubNub SDK to program the netowrk, to publish data streams or to subscribe to data streams.

Though the example is simple, it simulates as though multiple devices and sensors are registering themselves or announcing their availability by publishing their state on a dedicated channel. In reality this could be a deployment of meters or sensors in an area code that you wish to monitor for tial and do some realtime analysis using Spark.

Also, as an optional extension, the app can write to a socket or a directory where a Spark Streaming is monitoring for live
live data streams of JSON objects from each device. For directory monitoring of new files, the Spark application must run on the same JVM as this app and in Spark's local mode.

I employ a thread that simulates mulitple devices acting as publishers, but in reality each JSON data object could be published separately by each device using PubNub's publish-subscribe API. 

It downloads a list of words from the [Internet] (http://www.textfixer.com/resources/common-english-words.txt) and uses them as device names. Each JSON object has the 
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
As a developer, one huge attraction is how easy it's to write a PubNub publisher, using its SDK: a) instantiate an Pubnub handle b) define the right async callbacks, and c) publish data or message to a channel. 

All the complexity and reliablity is handled and hidden by the network. That's is huge productivity win for a developer who wants to connect devices and transmit realtime or periodic data to single or multiper subscribers listening on channels on the data network.

 To run this program to create json files into the destinattion directory for Spark Streaming consumption:
     `$ python pub_dev_words.py -u http://www.textfixer.com/resources/common-english-words.txt -c devices -i 1 -d data`

###pubnub_dir_streaming.py (Subscriber)
 This short example demonstrates how to consume a JSON dataset stream from directory. A publisher writes dataset into files into a destination directory.

Its counter part PubNub publisher, *pub_dev_words.py*, publishes to a channel and also writes JSON data to a data directory
for this Spark Streaming program to consume. While it does not diretory use PubNub subscriber API to get the item, the next
step is to modify this app so that it employ's PubNub's subscribe channel to recieve publishe data (on the to do list).

(At the moment I having trouble getting publish.subscribe() to work. But for now this work around suffices for running both publisher (*pub_dev_workds.py*) and subscriber (*pubnub_dir_streaming.py*) on the same machine.)

Ideally, you want the this Spark app to run on the cluster and directly subscrbe from the PubNub Data Network Stream.
(stay tune... coming soon)


Though short and simple, it illustrates Spark's brevity in doing more with little. 

Simplicity does not preclude profundity. One can achieve a lot by doing little, and that has been the appeal and draw of Spark Core API.

    `$ bin/spark-submit pubnub_dir_streaming.py data_dir`
##Requirements

In order to run these two applications you will need the following:
- Trial account with PubNub
- Install PubNub Python SDK 
- Apache Spark on your local machine running in local mode
- InfluxDB and Python SDK

##TO DO
1. Integrate with InfluxDB
2. Fix PubNub subscribe() keys issues
3. Include Pubnub.subscribe() calls within the Spark Streaming App
