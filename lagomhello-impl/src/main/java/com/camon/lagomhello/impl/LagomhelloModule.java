/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhello.impl;

import com.google.inject.AbstractModule;
import com.lightbend.lagom.javadsl.server.ServiceGuiceSupport;
import com.camon.lagomhello.api.LagomhelloService;

/**
 * The module that binds the LagomhelloService so that it can be served.
 */
public class LagomhelloModule extends AbstractModule implements ServiceGuiceSupport {
  @Override
  protected void configure() {
    bindService(LagomhelloService.class, LagomhelloServiceImpl.class);
  }
}
