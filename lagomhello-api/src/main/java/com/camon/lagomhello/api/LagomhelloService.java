/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhello.api;

import static com.lightbend.lagom.javadsl.api.Service.named;
import static com.lightbend.lagom.javadsl.api.Service.pathCall;
import static com.lightbend.lagom.javadsl.api.Service.topic;

import akka.Done;
import akka.NotUsed;
import com.lightbend.lagom.javadsl.api.Descriptor;
import com.lightbend.lagom.javadsl.api.Service;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.api.broker.kafka.KafkaProperties;
import com.lightbend.lagom.javadsl.api.transport.Method;

/**
 * The lagomhello service interface.
 * <p>
 * This describes everything that Lagom needs to know about how to serve and
 * consume the LagomhelloService.
 */
public interface LagomhelloService extends Service {

  ServiceCall<NotUsed, String> hello();
  ServiceCall<NotUsed, String> helloUser(String id);

  @Override
  default Descriptor descriptor() {
    // @formatter:off
    return named("lagomhello").withCalls(
        pathCall("/hello", this::hello),
        pathCall("/hello/:name", this::helloUser)
      ).withAutoAcl(true);
    // @formatter:on
  }
}
