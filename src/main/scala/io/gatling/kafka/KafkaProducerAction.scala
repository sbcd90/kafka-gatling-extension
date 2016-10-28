package io.gatling.kafka

import io.gatling.commons.stats.{KO, OK}
import io.gatling.commons.util.TimeHelper
import io.gatling.core.action.{Action, ChainableAction}
import io.gatling.core.session.Session
import io.gatling.core.stats.StatsEngine
import io.gatling.core.stats.message.ResponseTimings
import org.apache.avro.Schema

class KafkaProducerAction[K, V](producerProtocol: KafkaProducerProtocol[K, V],
                          statsEngine: StatsEngine,
                          nextAction: Action, schema: Option[Schema] = None)
  extends ChainableAction {
  override def execute(session: Session): Unit = {
    val start = TimeHelper.nowMillis
    try {
      producerProtocol.call(session, schema)
      val end = TimeHelper.nowMillis

      statsEngine.logResponse(session, "test",
        ResponseTimings(start, end), OK, Some("Success"), None)
    } catch {
      case e: Exception => statsEngine.logResponse(session, "test",
        ResponseTimings(start, start), KO, Some(e.getMessage), None)
    }
    nextAction ! session
  }

  override def next: Action = {
    nextAction
  }

  override def name: String = {
    getClass.getName
  }
}
