package io.gatling.kafka

import io.gatling.core.Predef.Session
import io.gatling.core.protocol.Protocol
import io.gatling.data.generator.RandomDataGenerator
import org.apache.avro.generic.GenericData.Record
import org.apache.avro.{Schema, SchemaBuilder}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaProducerProtocol[K: Manifest, V: Manifest](props: java.util.HashMap[String, Object],
                                  topics: String,
                                  dataGenerator: RandomDataGenerator[K, V] = null)
  extends Protocol {
  private final val kafkaProducer = new KafkaProducer[K, V](props)

  private var key: K = _
  private var value: V = _

  def call(session: Session,
           schema: Option[Schema] = None): Unit = {
    val attributes = session.attributes

    if (attributes.nonEmpty) {
      if (manifest[K].runtimeClass.isArray &&
          manifest[V].runtimeClass.isArray) {
        key = attributes.toString().getBytes().asInstanceOf[K]
        value = attributes.toString().getBytes().asInstanceOf[V]
      } else {
        key = createRecordForAvroSchema(attributes).asInstanceOf[K]
        value = createRecordForAvroSchema(attributes).asInstanceOf[V]
      }
    } else if (schema.nonEmpty) {
      key = dataGenerator.generateKey(schema)
      value = dataGenerator.generateValue(schema)
    } else {
      key = dataGenerator.generateKey()
      value = dataGenerator.generateValue()
    }

    val record = new ProducerRecord[K, V](topics, key, value)
    kafkaProducer.send(record)
  }

  private def createRecordForAvroSchema(attributes: Map[String, Any]): GenericRecord = {
    if (attributes.isEmpty) {
      throw new RuntimeException("attributes is empty. Cannot generate record")
    }

    val length = attributes.size

    var schemaBuilder = SchemaBuilder.record("testdata")
        .namespace("org.apache.avro").fields()

    for ((key, value) <- attributes) {
      schemaBuilder = schemaBuilder
        .name(key).`type`().nullable().stringType().noDefault()
    }
    val schema = schemaBuilder.endRecord()

    val avroRecord = new Record(schema)

    var count = 0
    for ((key, value) <- attributes) {
      avroRecord.put(count, value.toString)
      count += 1
    }
    avroRecord
  }
}