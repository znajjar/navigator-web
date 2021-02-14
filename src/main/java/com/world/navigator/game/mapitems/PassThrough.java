package com.world.navigator.game.mapitems;

import com.world.navigator.game.exceptions.ItemIsLockedException;

public interface PassThrough extends Checkable {
  int getNextRoomID() throws ItemIsLockedException;

  boolean canPassThrough();
}
