package com.world.navigator.game;

import com.world.navigator.game.player.Player;

import java.util.ArrayList;
import java.util.function.Consumer;

public class NotificationsManager {
  private final ArrayList<Consumer<Integer>> winningCallbacks;
  private final ArrayList<Runnable> timeUpCallbacks;
  private final ArrayList<Runnable> gameEndedCallbacks;
  private final GameTimer gameTimer;
  private int inGamePlayersCount;
  private int winnersCounter;

  public NotificationsManager() {
    winningCallbacks = new ArrayList<>();
    timeUpCallbacks = new ArrayList<>();
    gameEndedCallbacks = new ArrayList<>();
    gameTimer = new GameTimer();
  }

  public void setInGamePlayersCount(int inGamePlayersCount) {
    this.inGamePlayersCount = inGamePlayersCount;
  }

  public void registerWinningCallback(Consumer<Integer> winningCallback) {
    winningCallbacks.add(winningCallback);
  }

  public void registerTimeUpCallback(Runnable timeUpCallback) {
    timeUpCallbacks.add(timeUpCallback);
  }

  public void registerGameEndedCallback(Runnable gameEndedCallback) {
    gameEndedCallbacks.add(gameEndedCallback);
  }

  public Consumer<Player> getWinningCallback() {
    return this::winningRoomFoundCallback;
  }

  public void setTimer(long timeElapsed, long timeLimit) {
    gameTimer.setTimeLimit(timeLimit);
    gameTimer.setTimeElapsed(timeElapsed);
    gameTimer.setTimeUpCallback(this::timeUpCallback);
  }

  public void startTimer() {
    gameTimer.start();
  }

  private void timeUpCallback() {
    notifyTimeUpCallbacks();
    notifyGameEndedCallbacks();
  }

  private void notifyTimeUpCallbacks() {
    for (Runnable callback : timeUpCallbacks) {
      callback.run();
    }
  }

  private void winningRoomFoundCallback(Player winningPlayer) {
    winnersCounter++;
    if (winnersCounter == inGamePlayersCount) {
      notifyGameEndedCallbacks();
    }
    notifyWinningCallbacks(winningPlayer.getID());
  }

  private void notifyWinningCallbacks(int winnerID) {
    for (Consumer<Integer> callback : winningCallbacks) {
      callback.accept(winnerID);
    }
  }

  private void notifyGameEndedCallbacks() {
    for (Runnable callback : gameEndedCallbacks) {
      callback.run();
    }
  }

  public long getTimeElapsed() {
    return gameTimer.getTimeElapsed();
  }
}
