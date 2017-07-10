/*
 * Copyright (C) 2016 Lightbend Inc. <http://www.lightbend.com>
 */
package com.camon.lagomhello.impl;

import com.camon.lagomhello.api.LagomhelloEvent;
import com.lightbend.lagom.javadsl.persistence.PersistentEntity;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.camon.lagomhello.impl.LagomhelloCommand.*;

public class LagomhelloEntity extends PersistentEntity<LagomhelloCommand, LagomhelloEvent, LagomhelloState> {

  /**
   * An entity can define different behaviours for different states, but it will
   * always start with an initial behaviour. This entity only has one behaviour.
   */
  @Override
  public Behavior initialBehavior(Optional<LagomhelloState> snapshotState) {

    /*
     * Behaviour is defined using a behaviour builder. The behaviour builder
     * starts with a state, if this entity supports snapshotting (an
     * optimisation that allows the state itself to be persisted to combine many
     * events into one), then the passed in snapshotState may have a value that
     * can be used.
     */
    BehaviorBuilder b = newBehaviorBuilder(
        snapshotState.orElse(new LagomhelloState("HelloState", LocalDateTime.now().toString())));

    /*
     * Command handler for the Hello command.
     */
    b.setReadOnlyCommandHandler(HelloUser.class, (cmd, ctx) -> ctx.reply( "Hello, " + cmd.name + "!    "+ state().message + "_" + state().getTimestamp()));

    /*
     * We've defined all our behaviour, so build and return it.
     */
    return b.build();
  }

}
