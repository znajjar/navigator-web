package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerState;

public abstract class TradingPlayerCommand extends PlayerCommand {
  @Override
  boolean checkState(Player player) {
    return player.getState().equals(PlayerState.TRADING);
  }
}
