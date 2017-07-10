/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhello.impl;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;
import com.lightbend.lagom.serialization.Jsonable;
import lombok.Value;

/**
 * This interface defines all the commands that the LagomhelloEntity supports.
 * 
 * By convention, the commands should be inner classes of the interface, which
 * makes it simple to get a complete picture of what commands an entity
 * supports.
 */
public interface LagomhelloCommand extends Jsonable {

  @SuppressWarnings("serial")
  @Value
  @JsonDeserialize
  final class HelloUser implements LagomhelloCommand, PersistentEntity.ReplyType<String> {
    public final String name;

    @JsonCreator
    public HelloUser(String name) {
      this.name = name;
    }
  }

}
