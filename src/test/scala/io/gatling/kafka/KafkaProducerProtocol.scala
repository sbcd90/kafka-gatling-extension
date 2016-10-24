package io.gatling.kafka

import io.gatling.core.protocol.Protocol
import org.apache.avro.Schema
import org.apache.avro.Schema.Type
import org.apache.avro.generic.GenericData.Record
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

class KafkaProducerProtocol(kafkaProducer: KafkaProducer[GenericRecord, GenericRecord],
                            topics: String)
  extends Protocol {

  def call(schema: Schema): Unit = {
    val length = schema.getFields.size()

    val avroRecord = new Record(schema)
    for (i <- 0 until length) {
      val field = schema.getFields.get(i)
      val fieldType = field.schema().getType

      if (fieldType == Type.INT) {
        avroRecord.put(i, generateRandomData[Int](fieldType))
      } else if (fieldType == Type.STRING) {
        avroRecord.put(i, generateRandomData[String](fieldType))
      }
    }

    val record = new ProducerRecord[GenericRecord, GenericRecord](topics, avroRecord)
    kafkaProducer.send(record)
  }

  private def generateRandomData[T](fieldType: Type): T = {
    fieldType match {
      case Type.INT =>
        new scala.util.Random().nextInt(100).asInstanceOf[T]
      case Type.STRING =>
        new scala.util.Random().nextString(1).asInstanceOf[T]
    }
  }
}