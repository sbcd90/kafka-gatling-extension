package io.gatling.test

import java.util

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient
import io.confluent.kafka.serializers.KafkaAvroDeserializer
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import scala.collection.JavaConversions._

object KafkaAvroConsumer extends App {
  val kafkaTopic = "kafka_streams_testing398"
  val kafkaBrokers = "10.97.181.169:9092"

  val schemaRegistryUrl = "http://10.97.181.169:8081"
  val VALUE_SERIALIZATION_FLAG = "value"

  val props = new util.HashMap[String, Object]()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    classOf[KafkaAvroDeserializer])
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    classOf[KafkaAvroDeserializer])
  props.put("schema.registry.url", schemaRegistryUrl)
  props.put("group.id", "testgroup1")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("session.timeout.ms", "30000")

  val consumer = new KafkaConsumer[GenericRecord, GenericRecord](props)
  consumer.subscribe(util.Arrays.asList(kafkaTopic))

  // Get the schema of the topic
  val schemaRegistryClient = new CachedSchemaRegistryClient(schemaRegistryUrl, 1)
  val subject = kafkaTopic + "-" + VALUE_SERIALIZATION_FLAG
  val avroSchema = schemaRegistryClient.getLatestSchemaMetadata(subject).getSchema
  val parser = new Schema.Parser()
  val schema = parser.parse(avroSchema)

  while (true) {
    val records: ConsumerRecords[GenericRecord, GenericRecord] = consumer.poll(100)
    val recordsIterator: java.util.Iterator[ConsumerRecord[GenericRecord, GenericRecord]] = records.iterator()

    while (recordsIterator.hasNext) {
      val currentRecord = recordsIterator.next()

      val value = currentRecord.value()

      if (value.isInstanceOf[GenericRecord]) {
        val valueStruct = value

        for (field <- schema.getFields.toList) {
          print(valueStruct.get(field.name()))
        }
        println()
      }
    }
  }
}