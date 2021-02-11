package com.world.navigator.game.mapitems;

public interface PassThrough extends Checkable {
  int getNextRoomID();

  boolean canPassThrough();
}
