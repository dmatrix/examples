package main.scala.iot
/**
  * Licensed to the Apache Software Foundation (ASF) under one or more
  * contributor license agreements.  See the NOTICE file distributed with
  * this work for additional information regarding copyright ownership.
  * The ASF licenses this file to You under the Apache License, Version 2.0
  * (the "License"); you may not use this file except in compliance with
  * the License.  You may obtain a copy of the License at
  *
  *    http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */

import java.nio.ByteBuffer

import io.confluent.kafka.schemaregistry.client.{CachedSchemaRegistryClient, SchemaRegistryClient}
import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties
import org.apache.avro.Schema
import org.apache.avro.Schema.Type
import org.apache.avro.generic.{GenericRecord, GenericDatumReader}
import org.apache.avro.io.DecoderFactory
import org.apache.avro.util.Utf8
import org.apache.kafka.common.config.ConfigException
import org.apache.kafka.common.errors.SerializationException

class AvroDecoder(props: VerifiableProperties) extends Decoder[SchemaAndData] {
  var schemaRegistry: SchemaRegistryClient = null
  if (props == null) {
    throw new ConfigException("Missing schema registry url!")
  } else {
    val url = props.getProperty("schema.registry.url")
    if (url == null) {
      throw new ConfigException("Missing schema registry url!")
    } else {
      val maxSchemaObject = props.getInt("max.schemas.per.subject", 1000)
      schemaRegistry = new CachedSchemaRegistryClient(url, maxSchemaObject)
    }
  }

  def fromBytes(bytes: Array[Byte]): SchemaAndData = {
    val payload = getByteBuffer(bytes)
    val schemaId = payload.getInt

    SchemaAndData(schemaRegistry.getByID(schemaId), payload)
  }

  def getByteBuffer(payload: Array[Byte]): ByteBuffer = {
    val buffer = ByteBuffer.wrap(payload)
    if(buffer.get() != 0) {
      throw new SerializationException("Unknown magic byte!");
    } else {
      buffer
    }
  }
}

case class SchemaAndData(schema: Schema, data: ByteBuffer) {

  def deserialize(): AnyRef = {
    val messageLength = data.limit() - 1 - 4
    if (schema.getType == Schema.Type.BYTES) {
      val bytes = new Array[Byte](messageLength)
      data.get(bytes, 0, messageLength)
      bytes
    } else {
      val start = data.position() + data.arrayOffset()
      val reader = new GenericDatumReader[GenericRecord](schema)
      val obj: AnyRef = reader.read(null, DecoderFactory.get().binaryDecoder(data.array(), start, messageLength, null))
      if (schema.getType.equals(Type.STRING)) {
        obj.asInstanceOf[Utf8].toString
      } else {
        obj
      }
    }
  }
}

