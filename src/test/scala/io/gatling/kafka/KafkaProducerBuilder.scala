package io.gatling.kafka

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.protocol.Protocols
import io.gatling.core.structure.ScenarioContext
import org.apache.avro.Schema

case class KafkaProducerBuilder[K, V](schema: Option[Schema] = None) extends ActionBuilder {
  def kafkaProducerProtocol(protocols: Protocols): KafkaProducerProtocol[K, V] = {
    protocols.protocol[KafkaProducerProtocol[K, V]].getOrElse(
      throw new UnsupportedOperationException("KafkaProducerProtocol was not registered"))
  }

  override def build(ctx: ScenarioContext, next: Action): Action = {
    new KafkaProducerAction(
      kafkaProducerProtocol(ctx.protocolComponentsRegistry.protocols),
      ctx.coreComponents.statsEngine, next, schema)
  }
}