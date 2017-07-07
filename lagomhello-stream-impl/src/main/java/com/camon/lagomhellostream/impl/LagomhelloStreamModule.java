/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhellostream.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.camon.lagomhello.api.LagomhelloService;
import com.camon.lagomhellostream.api.LagomhelloStreamService;

/**
 * The module that binds the LagomhelloStreamService so that it can be served.
 */
public class LagomhelloStreamModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    // Bind the LagomhelloStreamService service
    bindService(LagomhelloStreamService.class, LagomhelloStreamServiceImpl.class);
    // Bind the LagomhelloService client
    bindClient(LagomhelloService.class);
    // Bind the subscriber eagerly to ensure it starts up
    bind(LagomhelloStreamSubscriber.class).asEagerSingleton();
  }
}
