package main.scala.iot

import kafka.serializer.DefaultDecoder
import org.apache.avro.Schema
import org.apache.avro.generic.GenericData.Record
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.util.Utf8
import org.apache.spark.SparkConf
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import io.confluent.kafka.serializers._


object DeviceIoTStreamApp {

    def main(args: Array[String]) : Unit = {

        if (args.length < 2) {
            println("Need 2 arguments: <kafka-broker:port> <topic>")
            System.exit(1)
        }

        println("Spark Streaming..here.. I come!")

        val Array(brokers, topics) = args

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

        val devicesRecords = deviceMessages.map(elem => {
            elem._2.deserialize().asInstanceOf[GenericRecord]
        })

        devicesRecords.print()
        

        // Start the computation
        ssc.start()
        ssc.awaitTermination()
    }
}
