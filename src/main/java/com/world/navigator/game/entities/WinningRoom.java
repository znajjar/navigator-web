package com.world.navigator.game.entities;

import com.world.navigator.game.player.Player;

import java.util.function.Consumer;

public class WinningRoom extends Room {
  private Consumer<Player> winningCallback;

  public WinningRoom(int id) {
    super(id);
  }

  public Consumer<Player> getWinningCallback() {
    return winningCallback;
  }

  public void setWinningCallback(Consumer<Player> winningCallback) {
    this.winningCallback = winningCallback;
  }
}
