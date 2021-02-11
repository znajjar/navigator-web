package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;

public class CheckPlayerCommand extends NavigationPlayerCommand {

  @Override
  PlayerEvent doCommand(Player player, String[] args) {
    return player.check();
  }

  @Override
  PlayerEvent getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedCheckResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerEvent getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedCheckResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "check";
  }
}
