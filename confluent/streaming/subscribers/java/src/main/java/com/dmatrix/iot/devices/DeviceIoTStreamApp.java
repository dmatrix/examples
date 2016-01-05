package com.dmatrix.iot.devices;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.util.HashMap;
import java.util.HashSet;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;


public final class DeviceIoTStreamApp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: DeviceIoTStreamApp <kafka-broker:port> <topic>");
            System.exit(1);
        }
        System.out.println("Spark Streaming Welcome!");

        String brokers = args[0];
        String topics = args[1];

        // Create context with a 30 seconds batch interval
        SparkConf sparkConf = new SparkConf().setAppName("DeviceIoTStreamApp");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(30));

        HashSet<String> topicsSet = new HashSet<String>();
        topicsSet.add(topics);
        HashMap<String, String> kafkaParams = new HashMap<String, String>();
        kafkaParams.put("metadata.broker.list", brokers);

        // Create direct kafka stream with brokers and topics
        JavaPairInputDStream<String, String> messages = KafkaUtils.createDirectStream(
                jssc,
                String.class,
                String.class,
                StringDecoder.class,
                StringDecoder.class,
                kafkaParams,
                topicsSet
        );

        JavaDStream<String> devices = messages.map(new Function<Tuple2<String, String>, String>() {
            @Override
            public String call(Tuple2<String, String> tuple2) {
                System.out.println("in map()...");
                return tuple2._2().toString();
            }
        });
        devices.foreach(new Function<JavaRDD<String>, Void>() {
            @Override
            public Void call(JavaRDD<String> stringJavaRDD) throws Exception {
                System.out.println("in foreach()...");
                System.out.println(stringJavaRDD);
                return null;
            }
        });
        devices.print();
        jssc.start();
        jssc.awaitTermination();

    }
}