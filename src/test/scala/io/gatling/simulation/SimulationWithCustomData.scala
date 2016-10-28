package io.gatling.simulation

import java.util

import io.gatling.core.Predef.{Simulation, _}
import io.gatling.kafka.{KafkaProducerBuilder, KafkaProducerProtocol}
import io.gatling.simulation.utils.RandomDataGeneratorExt
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.IntegerSerializer

import scala.concurrent.duration._

class SimulationWithCustomData extends Simulation {
  val kafkaTopic = "test_topic"
  val kafkaBrokers = "localhost:9092"

  val props = new util.HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[IntegerSerializer])

  val dataGenerator = new RandomDataGeneratorExt()
  val kafkaProducerProtocol = new KafkaProducerProtocol[Int, Int](props, kafkaTopic,
    dataGenerator)

  val scn = scenario("Sequential data simulation").exec(KafkaProducerBuilder[Int, Int]())

  setUp(scn.inject(constantUsersPerSec(1000) during (1 minute))).protocols(kafkaProducerProtocol)
}