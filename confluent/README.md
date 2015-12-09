#A Simple Producer (publisher) and Consumer (subscriber) for Confluent 2.0 with Apache Spark 0.9

![](images/confluent.png)

![source] (https://speakerdeck.com/nehanarkhede/demystifying-stream-processing-with-apache-kafka?utm_content=bufferd17aa&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer)

##Thou Shall Publish...Thy Shall Subscribe...
For as long as there have been printing papers, there have been publishers and consumers. 
In ancient times the King's scribes were the publishers, the pigeon the courier or transport, and remote Lords of the Houses the consumers or subscribers. In modern times, in the digital era, data is securely and reliably published and selectively subscribed.In other words, the publish/subscribe paradigm is not new; it's old.

In this short, simple putative rendition of HelloWorld equivalent of Publish/Subscribe paradigm programming model, I explore the Confluent Data Platform 2.0 (CDP), backed and supported by the developers and creators of Apache Kafka (0.9.0), orginally at LinkedIn.

The central idea and test is simple: how easy it's for me (or any developer) to do the following:
- Download and Install the platform
- Run it in local mode, not cluster mode.
- Ease or pain to write my first Hello World equivalent in two modes
- Command Line mode if supported
- Programmatically, in the supported SDK, of langage binding of my choice

I abide by the moto: *Less friction to development invariably leads to more (and rapid) adoption, stickness, and buzz*

The above example is derived from two sources:
- [The Confluent examples on Github] (https://github.com/dmatrix/examples-1/blob/master/README.md)
- [An IoT Device Simulation with PubNub- Part 1] (https://github.com/dmatrix/examples/blob/master/pubnub/py/README.md)

Later, I'll implement an example in which I'll employ CPD as the messaging system for simulating large scale IoT deployment simulation as done with [PubNub](https://www.linkedin.com/pulse/pubnub-integration-apache-spark-influxdb-simulation-iot-damji).

For now let's first crawl before we run...

##Relevant Files
###SimplePublisher.java (Producer)
As the name suggests, it's a simple producer of few fake devices state and publishes each device record to the CDP topic "devices." Three key takeaways. First, each topic to which you wish to publish a message, you must provide and register an Avro schema. For the duration of process (and even later) all producers publishing to this topic must adhere to this schema, which is registered and maintained in the Schema Registery. Second, since by default CDP uses Avoro ser/der for the messages, you get the benefit of most default data types out-of-the box. And finally, the Java API are fairly easy (I have not tried other client implementations; but it's worth exploring at least Scala, Python or Go).

I'll leave that as an exercise for other enthusiasts

###SimpleSubscriber.java (Consumer)
*TODO*
###Command Line Consumer (Consumer)
##Requirements
##Steps to Runs
##Watch the Runs
