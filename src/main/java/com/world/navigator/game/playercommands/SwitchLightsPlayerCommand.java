package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class SwitchLightsPlayerCommand extends NavigationPlayerCommand {

  @Override
  PlayerResponse doCommand(Player player, String[] args) {
    return player.interact().switchRoomLights();
  }

  @Override
  PlayerResponse getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedSwitchLightsResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerResponse getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedSwitchLightsResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "switchLights";
  }
}
