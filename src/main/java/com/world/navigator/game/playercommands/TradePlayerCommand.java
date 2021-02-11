package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.player.PlayerState;

public class TradePlayerCommand extends NavigationPlayerCommand {

  @Override
  PlayerEvent doCommand(Player player, String[] args) {
    return player.trade();
  }

  @Override
  PlayerEvent getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedTradeResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerEvent getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedTradeResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "trade";
  }

  @Override
  void updateState(Player player) {
    player.setState(PlayerState.TRADING);
  }
}
