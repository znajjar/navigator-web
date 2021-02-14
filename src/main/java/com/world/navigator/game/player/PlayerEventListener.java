package com.world.navigator.game.player;

public interface PlayerEventListener {
  void onResponse(String playerName, PlayerResponse response);

  void onEvent(String playerName, PlayerEvent event);
}
