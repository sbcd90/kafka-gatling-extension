package io.gatling.simulation

import java.util

import io.confluent.kafka.serializers.KafkaAvroSerializer
import io.gatling.core.Predef._
import io.gatling.data.generator.RandomDataGenerator
import io.gatling.kafka.{KafkaProducerBuilder, KafkaProducerProtocol}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.kafka.clients.producer.ProducerConfig

class SimulationWithAvroSchema extends Simulation {
  val kafkaTopic = "kafka_streams_testing398"
  val kafkaBrokers = "10.97.181.169:9092"

  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[KafkaAvroSerializer])
  props.put("schema.registry.url", "http://10.97.181.169:8081")

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

  val dataGenerator = new RandomDataGenerator[GenericRecord, GenericRecord]()
  val kafkaProducerProtocol = new KafkaProducerProtocol[GenericRecord, GenericRecord](props, kafkaTopic, dataGenerator)
  val scn = scenario("Kafka Producer Call").exec(KafkaProducerBuilder[GenericRecord, GenericRecord](Some(schema)))

  // constantUsersPerSec(100000) during (1 minute)
  setUp(scn.inject(atOnceUsers(1))).protocols(kafkaProducerProtocol)
}