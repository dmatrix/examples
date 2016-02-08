package com.dmatrix.iot.devices;

import io.confluent.kafka.serializers.KafkaAvroDecoder;
import org.apache.spark.api.java.JavaRDD;
import scala.Tuple2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Properties;

import kafka.serializer.StringDecoder;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.*;
import org.apache.spark.streaming.api.java.*;
import org.apache.spark.streaming.kafka.KafkaUtils;
import org.apache.spark.streaming.Durations;
import kafka.utils.VerifiableProperties;


public final class DeviceIoTStreamApp {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: DeviceIoTStreamApp <kafka-broker:port> <topic>");
            System.exit(1);
        }
        System.out.println("Spark Streaming Welcome!");

        Properties props = new Properties();
        props.put("zookeeper.connect", "localhost:2181");
        props.put("group.id", "group");
        props.put("auto.commit.enable", "false");
        props.put("auto.offset.reset", "smallest");
        props.put("schema.registry.url", "http://localhost:8081");

        VerifiableProperties vProps = new VerifiableProperties(props);

        // Create decoders for key and value
        KafkaAvroDecoder avroDecoder = new KafkaAvroDecoder(vProps);

        String brokers = args[0];
        String topics = args[1];

        // Create context with a 5 seconds batch interval
        SparkConf sparkConf = new SparkConf().setAppName("DeviceIoTStreamApp").set("spark.serializer", "org.apache.spark.serializer.KryoSerializer");
        JavaStreamingContext jssc = new JavaStreamingContext(sparkConf, Durations.seconds(5));
        jssc.checkpoint("devices");

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
                System.out.println("in map()...:" + tuple2.toString());
                return tuple2._2();
            }
        });
        devices.foreach(new Function<JavaRDD<String>, Void>() {
            @Override
            public Void call(JavaRDD<String> stringJavaRDD) throws Exception {
                System.out.println("in foreach()...");
                System.out.println(stringJavaRDD.rdd().toString());
                return null;
            }
        });
        devices.print();
        jssc.start();
        jssc.awaitTermination();

    }
}