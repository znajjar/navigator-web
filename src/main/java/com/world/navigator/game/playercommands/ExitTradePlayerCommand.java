package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.player.PlayerState;

public class ExitTradePlayerCommand extends TradingPlayerCommand {

  @Override
  PlayerEvent doCommand(Player player, String[] args) {
    return player.exitTrade();
  }

  @Override
  PlayerEvent getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedExitTradeResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerEvent getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedExitTradeResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "exitTrade";
  }

  @Override
  void updateState(Player player) {
    player.setState(PlayerState.NAVIGATING);
  }
}
