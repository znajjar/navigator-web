package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class BuyItemPlayerCommand extends TradingPlayerCommand {

  @Override
  PlayerResponse doCommand(Player player, String[] args) {
    String itemName = args[0];
    return player.trade().buy(itemName);
  }

  @Override
  boolean checkArgs(String[] args) {
    return args.length == 1;
  }

  @Override
  PlayerResponse getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedBuyResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerResponse getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedBuyResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "buy";
  }
}
