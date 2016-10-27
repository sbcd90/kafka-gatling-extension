package io.gatling.misc

import java.util

import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord, ConsumerRecords, KafkaConsumer}
import org.apache.kafka.common.serialization.StringDeserializer

object SimpleKafkaConsumer extends App {
  val kafkaTopic = "kafka_streams_testing698"
  val kafkaBrokers = "10.97.181.169:9092"

  val props = new util.HashMap[String, Object]()
  props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers)
  props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
    classOf[StringDeserializer])
  props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
    classOf[StringDeserializer])
  props.put("group.id", "test-group")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  props.put("session.timeout.ms", "30000")

  val consumer = new KafkaConsumer[String, String](props)

  consumer.subscribe(java.util.Arrays.asList(kafkaTopic))

  while (true) {
    val records: ConsumerRecords[String, String] = consumer.poll(100)
    val recordsIterator = records.iterator()

    while (recordsIterator.hasNext) {
      val currentRecord: ConsumerRecord[String, String] = recordsIterator.next()
      println(currentRecord.value())
    }
    Thread.sleep(1000)
  }
}