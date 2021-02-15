package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class ExitTradePlayerCommand extends TradingPlayerCommand {

  @Override
  public PlayerResponse execute(Player player, String[] args) {
      return RESPONSE_FACTORY.createSuccessfulExitTradeResponse();
  }

    @Override
    public PlayerResponse getInvalidStateResponse() {
        return RESPONSE_FACTORY.createFailedExitTradeResponse(INVALID_STATE_COMMENT);
    }

    @Override
    public PlayerResponse getInvalidArgsResponse() {
        return RESPONSE_FACTORY.createFailedExitTradeResponse(INVALID_ARGS_COMMENT);
    }

  @Override
  public String getName() {
    return "exitTrade";
  }

    @Override
    public void updateState(Player player) {
        player.state().navigating();
    }
}
