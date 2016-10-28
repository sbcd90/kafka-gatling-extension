package io.gatling.simulation

import java.util

import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.gatling.core.Predef._
import io.gatling.core.Predef.Simulation
import io.gatling.kafka.{KafkaProducerBuilder, KafkaProducerProtocol}
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.ProducerConfig

class FeederSimulationWithAvroSchema extends Simulation {
  val kafkaTopic = "kafka_streams_testing998"
  val kafkaBrokers = "10.97.181.169:9092"

  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put("schema.registry.url", "http://10.97.181.169:8081")

  val kafkaProducerProtocol =
    new KafkaProducerProtocol[GenericRecord, GenericRecord](props, kafkaTopic)

  val scn = scenario("Kafka Producer Call").feed(csv("test_data1.csv").circular)
    .exec(KafkaProducerBuilder[GenericRecord, GenericRecord]())

  setUp(scn.inject(atOnceUsers(5))).protocols(kafkaProducerProtocol)
}