package com.world.navigator.game.generator;

import com.world.navigator.game.playeritems.Key;

public class DoorPairDetails {
  private final Key key;
  private final boolean isLocked;

  public DoorPairDetails(Key key, boolean isLocked) {
    this.key = key;
    this.isLocked = isLocked;
  }

  public boolean isLocked() {
    return isLocked;
  }

  public Key getKey() {
    return key;
  }
}
