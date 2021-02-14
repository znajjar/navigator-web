package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class UseKeyPlayerCommand extends NavigationPlayerCommand {

  @Override
  PlayerResponse doCommand(Player player, String[] args) {
    String keyName = args[0];
    return player.interact().useKey(keyName);
  }

  @Override
  boolean checkArgs(String[] args) {
    return args.length == 1;
  }

  @Override
  PlayerResponse getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedUseKeyResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerResponse getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedUseKeyResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "useKey";
  }
}
