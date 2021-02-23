package com.world.navigator.game.entities;

import com.world.navigator.game.exceptions.ItemIsLockedException;

public interface PassThrough extends Checkable {
  Room getNextRoom() throws ItemIsLockedException;
}
