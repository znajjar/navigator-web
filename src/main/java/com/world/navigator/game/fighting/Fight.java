package com.world.navigator.game.fighting;

import com.world.navigator.game.player.Player;

public class Fight {
  private TieBreaker tieBreaker;
  private boolean isFinished;
  private Player firstPlayer;
  private Player secondPlayer;
  private String firstSubmission;
  private String secondSubmission;
  private Fight nextFight;
  private Player winner;

  public void setTieBreaker(TieBreaker tieBreaker) {
    this.tieBreaker = tieBreaker;
  }

  public void setFirstPlayer(Player firstPlayer) {
    firstPlayer.fight().getIntoFight();
    this.firstPlayer = firstPlayer;
  }

  public void setSecondPlayer(Player secondPlayer) {
    secondPlayer.fight().getIntoFight();
    this.secondPlayer = secondPlayer;
    start();
  }

  public void secondPlayerIsWinnerOf(Fight previousFight) {
    previousFight.nextFight(this);
  }

  private synchronized void nextFight(Fight nextFight) {
    if (nextFight.isFinished) {
      nextFight.winnerFromPreviousFight(winner);
    }

    this.nextFight = nextFight;
  }

  private void winnerFromPreviousFight(Player secondPlayer) {
    secondPlayer.fight().getIntoFight();
    this.secondPlayer = secondPlayer;
    start();
  }

  private void start() {
    int firstPlayerPoints = firstPlayer.loot().getPoints();
    int secondPlayerPoints = secondPlayer.loot().getPoints();
    if (firstPlayerPoints > secondPlayerPoints) {
      firstPlayerWins();
    } else if (secondPlayerPoints > firstPlayerPoints) {
      secondPlayerWins();
    } else {
      firstPlayer.fight().sendFight(tieBreaker.getMessage(), tieBreaker.getExpectedAnswers());
    }
  }

  public boolean isValidAnswer(String answer) {
    return tieBreaker.isValidAnswer(answer);
  }

  public synchronized void submitAnswerFrom(int playerId, String submission) {
    if (playerId == firstPlayer.getID() && firstSubmission != null) {
      firstSubmission = submission;
    } else if (playerId == secondPlayer.getID() && secondSubmission != null) {
      secondSubmission = submission;
    } else {
      throw new IllegalArgumentException("player is not in this fight");
    }

    if (firstSubmission != null && secondSubmission != null) {
      processResults();
    }
  }

  private void processResults() {
    if (tieBreaker.firstWins(firstSubmission, secondSubmission)) {
      firstPlayerWins();
    } else {
      secondPlayerWins();
    }
    isFinished = true;
    notifyNextFight();
  }

  private void firstPlayerWins() {
    winner = firstPlayer;
    firstPlayer.fight().winFight();
    secondPlayer.fight().loseFight();
    fightFinished();
  }

  private void secondPlayerWins() {
    winner = secondPlayer;
    firstPlayer.fight().loseFight();
    secondPlayer.fight().winFight();
    fightFinished();
  }

  private synchronized void fightFinished() {
    isFinished = true;
    notifyNextFight();
  }

  private void notifyNextFight() {
    if (nextFight != null) {
      nextFight.winnerFromPreviousFight(winner);
    }
  }
}
