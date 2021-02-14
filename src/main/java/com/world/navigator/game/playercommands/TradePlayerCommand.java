package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class TradePlayerCommand extends NavigationPlayerCommand {

  @Override
  PlayerResponse doCommand(Player player, String[] args) {
    return player.trade().trade();
  }

  @Override
  PlayerResponse getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedTradeResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerResponse getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedTradeResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "trade";
  }

  @Override
  void updateState(Player player) {
    player.state().trading();
  }
}
