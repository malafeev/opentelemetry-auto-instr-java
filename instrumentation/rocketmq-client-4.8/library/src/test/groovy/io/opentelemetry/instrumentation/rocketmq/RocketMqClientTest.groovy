/*
 * Copyright The OpenTelemetry Authors
 * SPDX-License-Identifier: Apache-2.0
 */

package io.opentelemetry.instrumentation.rocketmq

import io.opentelemetery.instrumentation.rocketmq.AbstractRocketMqClientTest
import io.opentelemetry.instrumentation.api.config.Config
import io.opentelemetry.instrumentation.test.LibraryTestTrait
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
import org.apache.rocketmq.client.producer.DefaultMQProducer
import spock.lang.Ignore

@Ignore("https://github.com/open-telemetry/opentelemetry-java-instrumentation/issues/2586")
class RocketMqClientTest extends AbstractRocketMqClientTest implements LibraryTestTrait {

  @Override
  void configureMQProducer(DefaultMQProducer producer) {
    producer.getDefaultMQProducerImpl().registerSendMessageHook(RocketMqTracing.newBuilder(openTelemetry)
      .setCaptureExperimentalSpanAttributes(
        Config.get()
          .getBooleanProperty(
            "otel.instrumentation.rocketmq-client.experimental-span-attributes", true))
      .build().newTracingSendMessageHook())
  }

  @Override
  void configureMQPushConsumer(DefaultMQPushConsumer consumer) {
    consumer.getDefaultMQPushConsumerImpl().registerConsumeMessageHook(RocketMqTracing.newBuilder(openTelemetry)
      .setCaptureExperimentalSpanAttributes(
        Config.get()
          .getBooleanProperty(
            "otel.instrumentation.rocketmq-client.experimental-span-attributes", true))
      .build().newTracingConsumeMessageHook())
  }
}