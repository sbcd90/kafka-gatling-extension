kafka-gatling-extension
=======================

[`Gatling`](http://gatling.io/#/) is an open-source load testing framework.
The `Kafka Gatling extension` can be used for stress testing an existing Apache Kafka installation using `Gatling`.


## Compatibility

The extension supports `Apache Kafka 0.10 protocol` & latest released version of Gatling `2.2`.

## Getting started

- Here is how we can create a simple Simulation.

```
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
```