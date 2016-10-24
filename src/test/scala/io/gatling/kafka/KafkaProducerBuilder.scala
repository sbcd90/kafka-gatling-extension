package io.gatling.kafka

import io.gatling.core.action.Action
import io.gatling.core.action.builder.ActionBuilder
import io.gatling.core.protocol.Protocols
import io.gatling.core.structure.ScenarioContext
import org.apache.avro.Schema

case class KafkaProducerBuilder(schema: Schema) extends ActionBuilder {
  def kafkaProducerProtocol(protocols: Protocols): KafkaProducerProtocol = {
    protocols.protocol[KafkaProducerProtocol].getOrElse(
      throw new UnsupportedOperationException("KafkaProducerProtocol was not registered"))
  }

  override def build(ctx: ScenarioContext, next: Action): Action = {
    new KafkaProducerAction(
      kafkaProducerProtocol(ctx.protocolComponentsRegistry.protocols),
      schema,
      ctx.coreComponents.statsEngine, next)
  }
}