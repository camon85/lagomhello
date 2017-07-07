/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhellostream.impl;

import akka.Done;
import akka.stream.javadsl.Flow;
import com.camon.lagomhello.api.LagomhelloEvent;
import com.camon.lagomhello.api.LagomhelloService;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

/**
 * This subscribes to the LagomhelloService event stream.
 */
public class LagomhelloStreamSubscriber {

  @Inject
  public LagomhelloStreamSubscriber(LagomhelloService lagomhelloService, LagomhelloStreamRepository repository) {
    // Create a subscriber
    lagomhelloService.helloEvents().subscribe()
      // And subscribe to it with at least once processing semantics.
      .atLeastOnce(
        // Create a flow that emits a Done for each message it processes
        Flow.<LagomhelloEvent>create().mapAsync(1, event -> {

          if (event instanceof LagomhelloEvent.GreetingMessageChanged) {
            LagomhelloEvent.GreetingMessageChanged messageChanged = (LagomhelloEvent.GreetingMessageChanged) event;
            // Update the message
            return repository.updateMessage(messageChanged.getName(), messageChanged.getMessage());

          } else {
            // Ignore all other events
            return CompletableFuture.completedFuture(Done.getInstance());
          }
        })
      );

  }
}
