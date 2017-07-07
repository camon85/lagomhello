/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhello.impl;

import akka.Done;
import akka.NotUsed;
import akka.japi.Pair;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.api.broker.Topic;
import com.lightbend.lagom.javadsl.broker.TopicProducer;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;

import com.camon.lagomhello.api.GreetingMessage;
import com.camon.lagomhello.api.LagomhelloService;
import com.camon.lagomhello.impl.LagomhelloCommand.*;

/**
 * Implementation of the LagomhelloService.
 */
public class LagomhelloServiceImpl implements LagomhelloService {

  private final PersistentEntityRegistry persistentEntityRegistry;

  @Inject
  public LagomhelloServiceImpl(PersistentEntityRegistry persistentEntityRegistry) {
    this.persistentEntityRegistry = persistentEntityRegistry;
    persistentEntityRegistry.register(LagomhelloEntity.class);
  }

  @Override
  public ServiceCall<NotUsed, String> hello(String id) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<LagomhelloCommand> ref = persistentEntityRegistry.refFor(LagomhelloEntity.class, id);
      // Ask the entity the Hello command.
      return ref.ask(new Hello(id));
    };
  }

  @Override
  public ServiceCall<GreetingMessage, Done> useGreeting(String id) {
    return request -> {
      // Look up the hello world entity for the given ID.
      PersistentEntityRef<LagomhelloCommand> ref = persistentEntityRegistry.refFor(LagomhelloEntity.class, id);
      // Tell the entity to use the greeting message specified.
      return ref.ask(new UseGreetingMessage(request.message));
    };

  }

  @Override
  public Topic<com.camon.lagomhello.api.LagomhelloEvent> helloEvents() {
    // We want to publish all the shards of the hello event
    return TopicProducer.taggedStreamWithOffset(LagomhelloEvent.TAG.allTags(), (tag, offset) ->

      // Load the event stream for the passed in shard tag
      persistentEntityRegistry.eventStream(tag, offset).map(eventAndOffset -> {

        // Now we want to convert from the persisted event to the published event.
        // Although these two events are currently identical, in future they may
        // change and need to evolve separately, by separating them now we save
        // a lot of potential trouble in future.
        com.camon.lagomhello.api.LagomhelloEvent eventToPublish;

        if (eventAndOffset.first() instanceof LagomhelloEvent.GreetingMessageChanged) {
          LagomhelloEvent.GreetingMessageChanged messageChanged = (LagomhelloEvent.GreetingMessageChanged) eventAndOffset.first();
          eventToPublish = new com.camon.lagomhello.api.LagomhelloEvent.GreetingMessageChanged(
            messageChanged.getName(), messageChanged.getMessage()
          );
        } else {
          throw new IllegalArgumentException("Unknown event: " + eventAndOffset.first());
        }

        // We return a pair of the translated event, and its offset, so that
        // Lagom can track which offsets have been published.
        return Pair.create(eventToPublish, eventAndOffset.second());
      })
    );
  }
}
