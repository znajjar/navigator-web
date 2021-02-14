package com.world.navigator.game.fighting;

import java.util.Arrays;

public class RockPaperScissorsTieBreaker implements TieBreaker {
  @Override
  public String getMessage() {
    return "Rock, Paper,Scissors.";
  }

  @Override
  public String[] getExpectedAnswers() {
    return Arrays.stream(Symbol.values()).map(Enum::name).toArray(String[]::new);
  }

  @Override
  public boolean isValidAnswer(String answer) {
    return Arrays.stream(Symbol.values()).anyMatch(symbol -> symbol.name().equals(answer));
  }

  @Override
  public boolean firstWins(String firstSubmission, String secondSubmission) {
    firstSubmission = firstSubmission.toUpperCase();
    secondSubmission = secondSubmission.toLowerCase();
    Symbol first = Symbol.valueOf(firstSubmission);
    Symbol second = Symbol.valueOf(secondSubmission);
    return first.beats(second);
  }

  private enum Symbol {
    ROCK,
    PAPER,
    SCISSOR;

    boolean beats(Symbol otherSymbol) {
      return switch (this) {
        case ROCK -> otherSymbol == SCISSOR;
        case PAPER -> otherSymbol == ROCK;
        case SCISSOR -> otherSymbol == PAPER;
      };
    }
  }
}
