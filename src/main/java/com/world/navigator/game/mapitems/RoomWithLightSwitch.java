package com.world.navigator.game.mapitems;

import com.world.navigator.game.Direction;
import com.world.navigator.game.playeritems.Flashlight;

public class RoomWithLightSwitch extends Room {
  private boolean lit;

  public RoomWithLightSwitch(int id, boolean lit) {
    super(id);
    this.lit = lit;
  }

  public boolean switchLights() {
    lit ^= true;
    return lit;
  }

  public boolean isLit() {
    return lit;
  }

  @Override
  public boolean canLookInDirectionWithFlashLight(Direction direction, Flashlight flashlight) {
    return super.canLookInDirectionWithFlashLight(direction, flashlight) || lit;
  }
}
