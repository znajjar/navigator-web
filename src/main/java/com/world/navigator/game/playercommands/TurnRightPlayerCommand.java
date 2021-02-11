package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;

public class TurnRightPlayerCommand extends NavigationPlayerCommand {

  @Override
  PlayerEvent doCommand(Player player, String[] args) {
    return player.turnRight();
  }

  @Override
  PlayerEvent getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedTurnResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerEvent getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedTurnResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "turnRight";
  }
}
