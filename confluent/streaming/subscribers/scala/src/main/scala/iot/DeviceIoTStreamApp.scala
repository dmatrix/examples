package main.scala.iot

import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import org.apache.spark.SparkConf


object DeviceIoTStreamApp {

  def main(args: Array[String]): Unit = {

    if (args.length < 2 ) {
      println("Need three arguments: <topic> <kafka-broker:port>")
      System.exit(1)
    }

    println("Spark Streaming..here.. I come!")

    val Array(brokers, topics) = args

    // Create context with 2 second batch interval
    val sparkConf = new SparkConf().setAppName("DeviceIoTStreamApp")
    val ssc = new StreamingContext(sparkConf, Seconds(2))

    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)

    val topicsSet = topics.toSet

    val deviceMessages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoderr]
                                                      (ssc, kafkaParams, topicsSet)


    // Start the computation
    ssc.start()
    ssc.awaitTermination()


  }
}
