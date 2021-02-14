package com.world.navigator.game.player;

public interface PlayerEvent {
  String getEventType();

  void put(String key, String value);

  void put(String key, String[] value);
}
