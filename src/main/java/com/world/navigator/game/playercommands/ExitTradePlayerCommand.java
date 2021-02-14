package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class ExitTradePlayerCommand extends TradingPlayerCommand {

  @Override
  PlayerResponse doCommand(Player player, String[] args) {
    return RESPONSE_FACTORY.createSuccessfulExitTradeResponse();
  }

  @Override
  PlayerResponse getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedExitTradeResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerResponse getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedExitTradeResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "exitTrade";
  }

  @Override
  void updateState(Player player) {
    player.state().navigating();
  }
}
