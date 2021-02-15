package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;
import com.world.navigator.game.player.PlayerResponseFactory;

public abstract class PlayerCommand {
  static final PlayerResponseFactory RESPONSE_FACTORY = PlayerResponseFactory.getInstance();
  static final String INVALID_STATE_COMMENT = "Invalid state";
  static final String INVALID_ARGS_COMMENT = "Invalid arguments";

  public boolean checkArgs(String[] args) {
    return args.length == 0;
  }

  public abstract boolean checkState(Player player);

  public abstract PlayerResponse execute(Player player, String[] args);

  public abstract PlayerResponse getInvalidStateResponse();

  public abstract PlayerResponse getInvalidArgsResponse();

  public abstract String getName();

  public void updateState(Player player) {
  }
}
