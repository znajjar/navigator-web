package com.world.navigator.game.entities;

import com.world.navigator.game.playeritems.Key;

public class Painting extends KeyHidingSpot {
  public Painting() {
    super();
  }

  public Painting(Key key) {
    super(key);
  }

  @Override
  public String look() {
    return "painting";
  }
}
