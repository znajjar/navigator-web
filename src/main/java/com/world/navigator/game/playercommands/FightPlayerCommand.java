package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;

abstract class FightPlayerCommand extends PlayerCommand {
  @Override
  boolean checkState(Player player) {
    return player.state().isFighting();
  }
}
