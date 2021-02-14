package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;
import com.world.navigator.game.player.PlayerResponseFactory;

public abstract class PlayerCommand {
  static final PlayerResponseFactory RESPONSE_FACTORY = PlayerResponseFactory.getInstance();
  static final String INVALID_STATE_COMMENT = "Invalid state";
  static final String INVALID_ARGS_COMMENT = "Invalid arguments";

  public final void execute(Player player, String[] args) {
    if (!checkArgs(args)) {
      player.listeners().notifyListenersOnResponse(getInvalidArgsResponse());
    } else if (!checkState(player)) {
      player.listeners().notifyListenersOnResponse(getInvalidStateResponse());
    } else {
      PlayerResponse event = doCommand(player, args);
      player.listeners().notifyListenersOnResponse(event);
      if (event.isSuccessful()) {
        updateState(player);
      }
    }
  }

  boolean checkArgs(String[] args) {
    return args.length == 0;
  }

  abstract boolean checkState(Player player);

  abstract PlayerResponse doCommand(Player player, String[] args);

  abstract PlayerResponse getInvalidStateResponse();

  abstract PlayerResponse getInvalidArgsResponse();

  public abstract String getName();

  void updateState(Player player) {}
}
