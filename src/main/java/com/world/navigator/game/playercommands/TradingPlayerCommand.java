package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;

public abstract class TradingPlayerCommand extends PlayerCommand {
  @Override
  boolean checkState(Player player) {
    return player.state().isTrading();
  }
}
