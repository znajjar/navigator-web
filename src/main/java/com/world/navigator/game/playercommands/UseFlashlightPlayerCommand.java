package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class UseFlashlightPlayerCommand extends NavigationPlayerCommand {

  @Override
  public PlayerResponse execute(Player player, String[] args) {
      return player.interact().useFlashlight();
  }

    @Override
    public PlayerResponse getInvalidStateResponse() {
        return RESPONSE_FACTORY.createFailedUseFlashlightResponse(INVALID_STATE_COMMENT);
    }

    @Override
    public PlayerResponse getInvalidArgsResponse() {
        return RESPONSE_FACTORY.createFailedUseFlashlightResponse(INVALID_ARGS_COMMENT);
    }

  @Override
  public String getName() {
    return "useFlashlight";
  }
}
