package com.world.navigator.game.fighting;

public interface TieBreaker {
  String getMessage();

  String[] getExpectedAnswers();

  boolean isValidAnswer(String answer);

  boolean firstWins(String firstSubmission, String secondSubmission);
}
