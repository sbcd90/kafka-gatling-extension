package io.gatling.simulation

import java.util

import io.gatling.core.Predef._
import io.gatling.core.Predef.Simulation
import io.gatling.kafka.{KafkaProducerBuilder, KafkaProducerProtocol}
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.ByteArraySerializer

class FeederByteArraySimulation extends Simulation {
  val kafkaTopic = "test_topic"
  val kafkaBrokers = "localhost:9092"

  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    classOf[ByteArraySerializer])
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    classOf[ByteArraySerializer])

  val kafkaProducerProtocol =
    new KafkaProducerProtocol[Array[Byte], Array[Byte]](props, kafkaTopic)
  val scn = scenario("Kafka Producer Call")
    .feed(csv("test_data1.csv").circular)
    .exec(KafkaProducerBuilder[Array[Byte], Array[Byte]]())

  setUp(scn.inject(atOnceUsers(5))).protocols(kafkaProducerProtocol)
}