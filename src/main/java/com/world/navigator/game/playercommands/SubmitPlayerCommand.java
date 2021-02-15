package com.world.navigator.game.playercommands;

import com.world.navigator.game.player.Player;
import com.world.navigator.game.player.PlayerResponse;

public class SubmitPlayerCommand extends FightPlayerCommand {
  @Override
  public boolean checkArgs(String[] args) {
      return args.length == 1;
  }

    @Override
    public PlayerResponse execute(Player player, String[] args) {
        return player.fight().submitAnswer(args[0]);
    }

    @Override
    public PlayerResponse getInvalidStateResponse() {
        return RESPONSE_FACTORY.createFailedSubmitResponse(INVALID_STATE_COMMENT);
    }

    @Override
    public PlayerResponse getInvalidArgsResponse() {
        return RESPONSE_FACTORY.createFailedSubmitResponse(INVALID_ARGS_COMMENT);
    }

  @Override
  public String getName() {
    return "submit";
  }
}
