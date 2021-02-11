package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;

public class SellItemPlayerCommand extends TradingPlayerCommand {

  @Override
  PlayerEvent doCommand(Player player, String[] args) {
    String itemName = args[0];
    return player.sell(itemName);
  }

  @Override
  boolean checkArgs(String[] args) {
    return args.length == 1;
  }

  @Override
  PlayerEvent getInvalidStateResponse() {
    return RESPONSE_FACTORY.createFailedSellResponse(INVALID_STATE_COMMENT);
  }

  @Override
  PlayerEvent getInvalidArgsResponse() {
    return RESPONSE_FACTORY.createFailedSellResponse(INVALID_ARGS_COMMENT);
  }

  @Override
  public String getName() {
    return "sell";
  }
}
