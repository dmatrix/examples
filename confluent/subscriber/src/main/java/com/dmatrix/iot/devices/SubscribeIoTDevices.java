/**
 * Copyright 2015 Confluent Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *  Adopted and changed from confluent github examples consumer directory to work
 *  my Iot Device Simulation.
 *
 *  @author Jules S. Damji
 */
package com.dmatrix.iot.devices;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import io.confluent.kafka.serializers.KafkaAvroDecoder;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;
import kafka.utils.VerifiableProperties;

public class SubscribeIoTDevices {
  private final ConsumerConnector consumer;
  private final String topic;
  private ExecutorService executor;
  private String zookeeper;
  private String groupId;
  private String url;

  public SubscribeIoTDevices(String zookeeper, String groupId, String topic, String url) {
    consumer = kafka.consumer.Consumer.createJavaConsumerConnector(
        new ConsumerConfig(createConsumerConfig(zookeeper, groupId, url)));
    this.topic = topic;
    this.zookeeper = zookeeper;
    this.groupId = groupId;
    this.url = url;
  }

  private Properties createConsumerConfig(String zookeeper, String groupId, String url) {
    Properties props = new Properties();
    props.put("zookeeper.connect", zookeeper);
    props.put("group.id", groupId);
    props.put("auto.commit.enable", "false");
    props.put("auto.offset.reset", "smallest");
    props.put("schema.registry.url", url);

    return props;
  }

  /**
   * Create an executor thread pool and bind N threads to it.
   * @param numThreads
     */
  public void run(int numThreads) {
    Map<String, Integer> topicCountMap = new HashMap<String, Integer>();
    topicCountMap.put(topic, numThreads);

    Properties props = createConsumerConfig(zookeeper, groupId, url);
    VerifiableProperties vProps = new VerifiableProperties(props);

    // Create decoders for key and value
    KafkaAvroDecoder avroDecoder = new KafkaAvroDecoder(vProps);

    Map<String, List<KafkaStream<Object, Object>>> consumerMap =
        consumer.createMessageStreams(topicCountMap, avroDecoder, avroDecoder);
    List<KafkaStream<Object, Object>> streams = consumerMap.get(topic);

    // Launch all the threads
    executor = Executors.newFixedThreadPool(numThreads);

    // Create ConsumerLogic objects and bind them to threads
    int threadNumber = 0;
    for (final KafkaStream stream : streams) {
      executor.submit(new ConsumeIoTDevices(stream, threadNumber));
      threadNumber++;
    }
  }

  public void shutdown() {
    if (consumer != null) consumer.shutdown();
    if (executor != null) executor.shutdown();
    try {
      if (!executor.awaitTermination(5000, TimeUnit.MILLISECONDS)) {
        System.out.println(
            "Timed out waiting for consumer threads to shut down, exiting uncleanly");
      }
    } catch (InterruptedException e) {
      System.out.println("Interrupted during shutdown, exiting uncleanly");
    }
  }

  public static void main(String[] args) {
    if (args.length != 5) {
      System.out.println("Please provide command line arguments: "
                         + "zookeeper groupId topic threads schemaRegistryUrl");
      System.exit(-1);
    }

    String zooKeeper = args[0];
    String groupId = args[1];
    String topic = args[2];
    int threads = Integer.parseInt(args[3]);
    String url = args[4];

    SubscribeIoTDevices example = new SubscribeIoTDevices(zooKeeper, groupId, topic, url);
    example.run(threads);

    try {
      Thread.sleep(20000);
    } catch (InterruptedException ie) {

    }
    example.shutdown();
  }
}
