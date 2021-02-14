package com.world.navigator.game.player;

import java.util.concurrent.CopyOnWriteArrayList;

public class ListenersManager {
  private final CopyOnWriteArrayList<PlayerEventListener> listeners;
  private final String playerName;

  public ListenersManager(String playerName) {
    this.playerName = playerName;
    listeners = new CopyOnWriteArrayList<>();
  }

  public void addListener(PlayerEventListener listener) {
    listeners.add(listener);
  }

  public void removeListener(PlayerEventListener listener) {
    listeners.remove(listener);
  }

  public void notifyListenersOnResponse(PlayerResponse response) {
    listeners.forEach((listener -> listener.onResponse(playerName, response)));
  }

  public void notifyListenersOnEvent(PlayerEvent event) {
    listeners.forEach((listener -> listener.onEvent(playerName, event)));
  }
}
