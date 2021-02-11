package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerEvent;
import com.world.navigator.game.player.PlayerEventFactory;

public abstract class PlayerCommand {
  static final PlayerEventFactory RESPONSE_FACTORY = PlayerEventFactory.getInstance();
  static final String INVALID_STATE_COMMENT = "Invalid state";
  static final String INVALID_ARGS_COMMENT = "Invalid arguments";

  public final void execute(Player player, String[] args) {
    if (checkState(player)) {
      player.notifyListeners(doCommand(player, args));
      updateState(player);
    } else {
      player.notifyListeners(getInvalidStateResponse());
    }
  }

  boolean checkArgs(String[] args) {
    return args.length == 0;
  }

  abstract boolean checkState(Player player);

  abstract PlayerEvent doCommand(Player player, String[] args);

  abstract PlayerEvent getInvalidStateResponse();

  abstract PlayerEvent getInvalidArgsResponse();

  public abstract String getName();

  void updateState(Player player) {}
}
