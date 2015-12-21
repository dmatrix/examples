package com.dmatrix.iot.devices;

import java.util.Properties;
import java.util.Date;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
/**
 * Created by jules on 12/8/15.
 * A simple illustration of how to use basic API to create a simple producer that publishes N number of device related
 * information on a topic "devices."
 *
 * Simple steps:
 *  1. Create a schema for your topic
 *  2. Create a message, in this case my JSON like doc, that adheres to the Avro Schema
 *  3. Verify the schema
 *  4. Create a handle to a Kafka producer
 *  5. Publish it!
 *  This example follows the same structure as the github example https://github.com/confluentinc/examples
 *
 */
public class SimplePublisher {

	private String topic;

	/**
	 *
	 * @param pTopic
     */
	public SimplePublisher(String pTopic)
	{
		topic = pTopic;
	}

	/**
	 *
	 * @return
     */
	public String getTopic()
	{
		return topic;
	}

	/**
	 * Build device record
	 * @param id
	 * @param schema
     * @return GenericRecord
     */
	public GenericRecord buildDeviceInfo(int id, Schema schema) {

		GenericRecord deviceRecord = new GenericData.Record(schema);
		deviceRecord.put("device_id", id);
		deviceRecord.put("device_name", IotDevices.getDeviceType(id));
		deviceRecord.put("ip", "192.34.5." + id);
		deviceRecord.put("temp", IotDevices.getTemp());
		deviceRecord.put("humidity", IotDevices.getHumidity());
		deviceRecord.put("lat", IotDevices.getCoordinate());
		deviceRecord.put("long", IotDevices.getCoordinate());
		deviceRecord.put("zipcode", IotDevices.getZipCode(id));
		deviceRecord.put("scale", "Celsius");
		deviceRecord.put("timestamp", new Date().getTime());

		return deviceRecord;

	}

	/**
	 *  Build a schema
	 * @return schema string
     */
	public String buildAvroSchema() {

		StringBuffer buffer = new StringBuffer("{\"namespace\": \"device.avro\", \"type\": \"record\", ");
		buffer.append("\"name\": \"devices\"," );
		buffer.append("\"fields\": [" );
		buffer.append("{\"name\": \"device_id\", \"type\": \"int\"}," );
		buffer.append("{\"name\": \"device_name\", \"type\": \"string\"}," );
		buffer.append("{\"name\": \"ip\", \"type\": \"string\"},");
		buffer.append("{\"name\": \"temp\", \"type\": \"int\"},");
		buffer.append("{\"name\": \"humidity\", \"type\": \"int\"},");
		buffer.append("{\"name\": \"lat\", \"type\": \"int\"},");
		buffer.append("{\"name\": \"long\", \"type\": \"int\"},");
		buffer.append("{\"name\": \"zipcode\", \"type\": \"int\"},");
		buffer.append("{\"name\": \"scale\", \"type\": \"string\"},");
		buffer.append("{\"name\": \"timestamp\", \"type\": \"long\"}");
		buffer.append("]}");

		return buffer.toString();

	}

	/**
	 *  Main driver for the publisher
	 * @param args Command line arguments
     */
	public static void main(String[] args) {
		if (args.length != 3) {
			System.out
					.println("Usage: SimplePublisher topic number schema-registry-URL");
			System.exit(1);
		}
		String topic = args[0];
		int numOfDevices;
		numOfDevices = Integer.valueOf(args[1]);
		String url= args[2];

		SimplePublisher sp = new SimplePublisher(topic);
		// Build properties
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("acks", "all");
		props.put("retries", 0);
		props.put("key.serializer",
				"io.confluent.kafka.serializers.KafkaAvroSerializer");
		props.put("value.serializer",
				"io.confluent.kafka.serializers.KafkaAvroSerializer");
		props.put("schema.registry.url", url);
		// Create a schema for the device JSON
		String schemaDeviceString = sp.buildAvroSchema();
		// Verify and parse schema
		Schema.Parser parser = new Schema.Parser();
		Schema schema = parser.parse(schemaDeviceString);

		// Instantiate a Kafka producer
		Producer<String, GenericRecord> producer = new KafkaProducer<String, GenericRecord>(props);
		// Create devices info based on the schema
		for (int id = 0; id < numOfDevices; id++) {
			GenericRecord deviceRec = sp.buildDeviceInfo(id, schema);
			// create a ProducerRecord
			ProducerRecord<String, GenericRecord> data = new ProducerRecord<String, GenericRecord>(topic, Integer.toString(id), deviceRec);
			// Publish it on the topic "devices."
			try {
				System.out.format("Device info publishing to Kafka topic %s : %s\n",
						sp.getTopic(), data.toString()) ;
				producer.send(data);
			} catch (Exception exec) {
				exec.printStackTrace();
			}
		}
		producer.close();
	}

}
