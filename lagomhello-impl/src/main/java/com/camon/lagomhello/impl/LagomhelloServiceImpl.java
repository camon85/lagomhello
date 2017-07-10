/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhello.impl;

import akka.NotUsed;
import com.camon.lagomhello.api.LagomhelloService;
import com.camon.lagomhello.impl.LagomhelloCommand.HelloUser;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRef;
import com.lightbend.lagom.javadsl.persistence.PersistentEntityRegistry;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;

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
  public ServiceCall<NotUsed, String> hello() {
    return request -> CompletableFuture.completedFuture("Hello world");
  }

  @Override
  public ServiceCall<NotUsed, String> helloUser(String id) {
    return request -> {
      PersistentEntityRef<LagomhelloCommand> ref = persistentEntityRegistry.refFor(LagomhelloEntity.class, id);
      return ref.ask(new HelloUser(id));
    };
  }

}
