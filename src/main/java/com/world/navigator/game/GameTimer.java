package com.world.navigator.game;

import java.util.TimerTask;

public class GameTimer {
  private final java.util.Timer timer;
  private volatile long timeElapsed;
  private long timeLimit;
  private Runnable timeUpCallback;

  public GameTimer() {
    timer = new java.util.Timer();
  }

  public long getTimeLimit() {
    return timeLimit;
  }

  public void setTimeLimit(long timeLimit) {
    this.timeLimit = timeLimit;
  }

  public long getTimeElapsed() {
    return timeElapsed;
  }

  public void setTimeElapsed(long timeElapsed) {
    this.timeElapsed = timeElapsed;
  }

  public void setTimeUpCallback(Runnable timeUpCallback) {
    this.timeUpCallback = timeUpCallback;
  }

  public void start() {
    timer.scheduleAtFixedRate(
        new TimerTask() {
          @Override
          public synchronized void run() {
            timeElapsed += 1;
            if (timeElapsed >= timeLimit) {
              timer.cancel();
              notifyTimeUpCallback();
            }
          }
        },
        0,
        1);
  }

  private void notifyTimeUpCallback() {
    timeUpCallback.run();
  }
}
