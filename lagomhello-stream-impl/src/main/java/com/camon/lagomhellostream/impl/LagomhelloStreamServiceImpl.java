/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhellostream.impl;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import com.lightbend.lagom.javadsl.api.ServiceCall;
import com.camon.lagomhello.api.LagomhelloService;
import com.camon.lagomhellostream.api.LagomhelloStreamService;

import javax.inject.Inject;

import static java.util.concurrent.CompletableFuture.completedFuture;

/**
 * Implementation of the LagomhelloStreamService.
 */
public class LagomhelloStreamServiceImpl implements LagomhelloStreamService {

  private final LagomhelloService lagomhelloService;
  private final LagomhelloStreamRepository repository;

  @Inject
  public LagomhelloStreamServiceImpl(LagomhelloService lagomhelloService, LagomhelloStreamRepository repository) {
    this.lagomhelloService = lagomhelloService;
    this.repository = repository;
  }

  @Override
  public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> directStream() {
    return hellos -> completedFuture(
        hellos.mapAsync(8, name -> lagomhelloService.hello(name).invoke()));
  }

  @Override
  public ServiceCall<Source<String, NotUsed>, Source<String, NotUsed>> autonomousStream() {
    return hellos -> completedFuture(
        hellos.mapAsync(8, name -> repository.getMessage(name).thenApply( message ->
            String.format("%s, %s!", message.orElse("Hello"), name)
        ))
    );
  }
}
