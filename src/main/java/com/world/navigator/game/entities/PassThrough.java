package com.world.navigator.game.entities;

import com.world.navigator.game.exceptions.ItemIsLockedException;

public interface PassThrough extends Checkable {
  int getNextRoomID() throws ItemIsLockedException;

  boolean canPassThrough();
}
