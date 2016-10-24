package io.gatling.simulation

import java.util

import io.gatling.core.Predef.Simulation
import io.gatling.core.Predef._
import io.gatling.data.generator.RandomDataGenerator
import io.gatling.kafka.{KafkaProducerBuilder, KafkaProducerProtocol}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer

class SimpleKafkaProducerSimulation extends Simulation {
  val kafkaTopic = "kafka_streams_testing498"
  val kafkaBrokers = "10.97.181.169:9092"

  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer])

  val dataGenerator = new RandomDataGenerator[String, String]()
  val kafkaProducerProtocol = new KafkaProducerProtocol[String, String](props, kafkaTopic,
    dataGenerator)
  val scn = scenario("Kafka Producer Call").exec(KafkaProducerBuilder[String, String]())

  setUp(scn.inject(atOnceUsers(1))).protocols(kafkaProducerProtocol)
}