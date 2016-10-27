package io.gatling.kafka

import io.gatling.core.protocol.Protocol
import io.gatling.data.generator.RandomDataGenerator
import org.apache.avro.Schema
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaProducerProtocol[K, V](props: java.util.HashMap[String, Object],
                                  topics: String,
                                  dataGenerator: RandomDataGenerator[K, V])
  extends Protocol {
  private final val kafkaProducer = new KafkaProducer[K, V](props)

  private var key: K = _
  private var value: V = _

  def call(schema: Option[Schema] = None): Unit = {
    if (schema.nonEmpty) {
      key = dataGenerator.generateKey(schema)
      value = dataGenerator.generateValue(schema)
    } else {
      key = dataGenerator.generateKey()
      value = dataGenerator.generateValue()
    }

    val record = new ProducerRecord[K, V](topics, key, value)
    kafkaProducer.send(record)
  }
}