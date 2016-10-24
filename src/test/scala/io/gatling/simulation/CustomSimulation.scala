package io.gatling.simulation

import java.util

import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.gatling.core.Predef._
import io.gatling.kafka.{KafkaProducerBuilder, KafkaProducerProtocol}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig}
import scala.concurrent.duration._

class CustomSimulation extends Simulation {
  val kafkaTopic = "kafka_streams_testing298"
  val kafkaBrokers = "10.97.183.115:9092,10.97.191.51:9092,10.97.152.59:9093,10.97.152.66:9093"

  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put("schema.registry.url", "http://10.97.183.115:8081")

  val producer = new KafkaProducer[GenericRecord, GenericRecord](props)

  val user_schema =
    s"""
       | {
       |    "fields": [
       |        { "name": "int1", "type": "int" }
       |    ],
       |    "name": "myrecord",
       |    "type": "record"
       |}
     """.stripMargin

  val schema = new Schema.Parser().parse(user_schema)

  val kafkaProducerProtocol = new KafkaProducerProtocol(producer, kafkaTopic)
  val scn = scenario("Kafka Producer Call").exec(KafkaProducerBuilder(schema))

  setUp(scn.inject(constantUsersPerSec(100000) during (1 minute))).protocols(kafkaProducerProtocol)
}