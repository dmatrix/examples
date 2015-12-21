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
 *  Adopted and changed from confluent github examples consumer directory to work with this
 *  my Iot Device Simulation.
 *
 *  @author Jules S. Damji
 */
package com.dmatrix.iot.devices;
import org.apache.avro.generic.GenericRecord;

import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.message.MessageAndMetadata;
import org.influxdb.*;
import org.influxdb.dto.Point;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Runnable as part of the executor thread pool that will read off device messages off a Kafka Stream
 */
public class ConsumeIoTDevices implements Runnable {
  private KafkaStream stream;
  private int threadNumber;

  public ConsumeIoTDevices (KafkaStream stream, int threadNumber) {
    this.threadNumber = threadNumber;
    this.stream = stream;
  }

  public Point generateInfluxDBPoint(GenericRecord deviceRecord, String measurement) {

      Map <String, String> tags = new HashMap<String, String>();
      // get message values
      long ts = Long.parseLong((deviceRecord.get("timestamp")).toString());
      int devId = Integer.parseInt((deviceRecord.get("device_id")).toString());
      String deviceName = (deviceRecord.get("device_name")).toString();
      int humidity = Integer.parseInt((deviceRecord.get("humidity")).toString());
      int temp = Integer.parseInt((deviceRecord.get("temp")).toString());
      int zipcode = Integer.parseInt((deviceRecord.get("zipcode")).toString());
      String ip = (deviceRecord.get("ip")).toString();
      int lat = Integer.parseInt((deviceRecord.get("lat")).toString());
      int longitude = Integer.parseInt((deviceRecord.get("long")).toString());
      String scale = (deviceRecord.get("scale")).toString();
      // create tags
      tags.put("device_id", Integer.toString(devId));
      tags.put("device_name", deviceName);
      tags.put("humidity", Integer.toString(humidity));
      tags.put("temp", Integer.toString(temp));
      tags.put("zipcode", Integer.toString(zipcode));

      Point influxPoint = Point.measurement(measurement)
            .time(ts,TimeUnit.MILLISECONDS)
            .tag(tags)
            .field("ip", ip)
            .field("lat", lat)
            .field("long", longitude)
            .field("scale", scale)
            .build();

      return influxPoint;
  }
  /**
   *  Fetch each record from the partition.
   */
  public void run() {

      boolean dbFailed = false;
      InfluxDBConnection influxDBConn = null;
      ConsumerIterator<Object, Object> it = stream.iterator();
      try {
          influxDBConn = InfluxDBConnection.getInstance();
      } catch (Exception ex) {
          ex.printStackTrace();
          dbFailed = true;
      }
      while (it.hasNext()) {
          MessageAndMetadata<Object, Object> record = it.next();
          String topic = record.topic();
          int partition = record.partition();
          long offset = record.offset();
          Object key = record.key();
          GenericRecord message = (GenericRecord) record.message();
          System.out.println("Thread " + threadNumber +
                  " received: " + "Topic " + topic +
                  " Partition " + partition +
                  " Offset " + offset +
                  " Key " + key +
                  " Message " + message.toString());
          if (!dbFailed) {
              // create two measurements points for respective tags and fields
              String[] measurements={"humidity", "temperature"};
              for (String measure: measurements) {
                  Point influxPoint = generateInfluxDBPoint(message, measure);
                  influxDBConn.writePoint(influxPoint);
                  System.out.println("Inserted InfluxDB:" + influxPoint.toString());
              }
          }
      }
      System.out.println("Shutting down Thread: " + threadNumber);
  }
}
