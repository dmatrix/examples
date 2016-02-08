package main.scala.iot

import kafka.serializer.DefaultDecoder
import org.apache.avro.generic.{GenericRecord}
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka._

object DeviceIoTStreamApp {

  /**
    * Filter the DStream as determined by the predicate function
    * @param rec GenericRecord, the device message
    * @param key filter by this key
    * @return true or false as determined by the predicate
    */
    def filterByKey(rec: GenericRecord, key: String, value : Int) : Boolean = {
        val  v = rec.get(key).asInstanceOf[Int]
        println("filterBy: " + key + " Processing record: " + rec.toString)
        println(key + " = " + v)
        return (v >= value)
    }

    def main(args: Array[String]) : Unit = {

        if (args.length < 4) {
            println("Need 2 arguments: <kafka-broker:port> <topic> device-filter value")
            println("DeviceToStream App localhost:9092 {temperature|humidity} value")
            System.exit(1)
        }

        println("Spark Streaming..here.. I come!")

        val Array(brokers, topics, filter, value) = args

        // Create context with 2 second batch interval
        val sparkConf = new SparkConf().setAppName("DeviceIoTStreamApp").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
        val ssc = new StreamingContext(sparkConf, Seconds(2))
        ssc.checkpoint("devices")

        var topicMap = Map[String, Int]()
        topicMap += (topics -> 1)

        var consumerConfig = Map[String, String]()
        consumerConfig += ("group.id" -> "group")
        consumerConfig += ("zookeeper.connect" -> "localhost:2181")
        consumerConfig += ("auto.offset.reset" -> "smallest")
        consumerConfig += ("metadata.broker.list" -> brokers)
        consumerConfig += ("schema.registry.url" -> "http://localhost:8081")


        val deviceMessages = KafkaUtils.createStream[Array[Byte], SchemaAndData, DefaultDecoder, AvroDecoder](ssc, consumerConfig, topicMap, StorageLevel.MEMORY_ONLY)

        // Use DStream.map() to serialize all GenericRecords and then use DStream.filter() to extract only records whose device temperature
        // or humidity is greater than or equal to given argument as evaluated in the filter predicate.
        val devicesRecords = deviceMessages.map(elem => {
            elem._2.deserialize().asInstanceOf[GenericRecord]
                    }).filter(e => filterByKey(e, filter, value.toInt))

        devicesRecords.print()
        // now use the case class DeviceInfo
      /**
        *  {"device_id": 0,
        * "device_name": "sensor-025ForJSD",
        * "ip": "192.34.5.0",
        * "temp": 35,
        * "humidity": 82,
        * "lat": 33,
        * "long": 8,
        * "zipcode": 94538,
        * "scale": "Celsius",
        * "timestamp": 1454785287191
        * }
        */
      val devices = devicesRecords.map(d => IOTDevice (d.get(0).asInstanceOf[Int],
                        d.get(1).asInstanceOf[String],
                        d.get(2).asInstanceOf[String],
                        d.get(3).asInstanceOf[Int],
                        d.get(4).asInstanceOf[Int],
                        d.get(5).asInstanceOf[Int],
                        d.get(6).asInstanceOf[Int],
                        d.get(7).asInstanceOf[Int],
                        d.get(8).asInstanceOf[String],
                        d.get(9).asInstanceOf[Long]))

      devices.print();

        // Start the receiver
        ssc.start()
        ssc.awaitTermination()
    }
}
