name := "main/scala/iot"
version := "1.0"

resolvers ++= Seq(
  "Confluent" at "http://packages.confluent.io/maven/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" % "spark-streaming_2.10" % "1.6.0",
  "org.apache.spark" % "spark-streaming-kafka_2.10" % "1.6.0",
  "org.apache.kafka" % "kafka-clients" % "0.8.2.1",
  "org.apache.kafka" % "kafka_2.10" % "0.8.2.1",
  "org.slf4j" % "slf4j-log4j12" % "1.6.1",
  "org.apache.zookeeper" % "zookeeper" % "3.4.5",
  "io.confluent" % "kafka-avro-serializer" % "1.0.1"
)

ivyScala := ivyScala.value map { _.copy(overrideScalaVersion = true) }

scalaVersion := "2.10.4"
