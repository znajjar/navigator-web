package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.player.PlayerState;

public class StatusPlayerCommand extends PlayerCommand {
  @Override
  boolean checkState(Player player) {
    return !player.getState().equals(PlayerState.WAITING);
  }

  @Override
  PlayerEvent doCommand(Player player, String[] args) {
    return player.getStatus();
  }

  @Override
  PlayerEvent getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedStatusResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerEvent getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedStatusResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "status";
  }
}
