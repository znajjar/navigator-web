package com.world.navigator.game.mapitems;

import com.world.navigator.game.playeritems.Key;

public class Mirror extends KeyHidingSpot {
  public Mirror() {
    super();
  }

  public Mirror(Key key) {
    super(key);
  }

  @Override
  public String look() {
    return "mirror";
  }
}
